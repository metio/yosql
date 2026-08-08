/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import wtf.metio.yosql.codegen.files.SqlText;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Finds the DDL a project describes its schema with, and reads it in the order it applies.
 *
 * <p>Deliberately not the statement parser. This runs before parsing so that what it learns is
 * available while statements are still being configured, and it must not be able to fail: a schema
 * it cannot read leaves the catalog emptier, which skips checks, where a thrown exception would stop
 * a build over a file nobody asked it to understand. So it reads text, splits it, and looks for one
 * front matter key.</p>
 *
 * <p>Files are read in the order a migration tool would apply them, which is what makes a directory
 * of migrations describe the schema they leave behind rather than an arbitrary one. See
 * {@link #APPLICATION_ORDER}.</p>
 */
public final class SchemaFiles {

    private static final Pattern VENDOR = Pattern.compile(
            "^--\\s*vendor\\s*:\\s*(.+?)\\s*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
    private static final Pattern FRONT_MATTER_LINE = Pattern.compile("^\\s*--.*$", Pattern.MULTILINE);

    /**
     * Flyway's versioned migration: {@code V}, a version of numeric segments separated by {@code .}
     * or {@code _}, then {@code __} and a description.
     */
    private static final Pattern VERSIONED_MIGRATION = Pattern.compile(
            "^[vV](\\d+(?:[._]\\d+)*)__.*");

    /**
     * Flyway's undo migration, which says how to reverse the versioned one of the same number.
     *
     * <p>It is never run by a migration, so the schema a project's database is in has never had it
     * applied and reading it describes a database that does not exist. Read as ordinary DDL it is
     * worse than useless: it carries no version this reader recognises, so it sorted after every
     * versioned migration and undid them — a {@code U48} holding {@code drop column update_window}
     * took that column back out of the catalog last of all, and reported nothing, because dropping
     * a column is something this reader follows perfectly well.</p>
     */
    private static final Pattern UNDO_MIGRATION = Pattern.compile(
            "^[uU]\\d+(?:[._]\\d+)*__.*");

    /**
     * The order the files applied in, which is not the order their names sort in.
     *
     * <p>A versioned migration is ordered by its version, segment by segment and each segment as a
     * number, because that is the order the tool that wrote the names applies them in. Sorting the
     * names as text puts {@code V10__} and {@code V12__} in front of {@code V2__}, so an
     * {@code alter table} lands before the {@code create table} it extends — where it is dropped,
     * since altering a table the catalog does not hold says nothing. The columns of every migration
     * numbered past nine then go missing, which reads downstream as a schema that disagrees with the
     * statements written against it and as parameter types that cannot be inferred. Renaming the
     * files is not a fix available to anyone whose migrations have already been applied.</p>
     *
     * <p>Anything else keeps name order, after the versioned migrations — where Flyway also runs its
     * repeatable {@code R__} ones, and where a project whose DDL is not migrations at all is
     * unaffected, since with no versions to order by this is name order throughout. Undo migrations
     * are the exception and are not read at all; see {@link #UNDO_MIGRATION}.</p>
     */
    private static final Comparator<Path> APPLICATION_ORDER = Comparator
            .comparing(SchemaFiles::version, Comparator.nullsLast(SchemaFiles::compareVersions))
            .thenComparing(Path::toString);

    private final FilesConfiguration files;
    private final SchemaConfiguration schema;

    public SchemaFiles(final FilesConfiguration files, final SchemaConfiguration schema) {
        this.files = files;
        this.schema = schema;
    }

    /**
     * @return every statement that might be DDL, with the vendor it was written for
     */
    public List<Schemas.VendorStatement> read() {
        final var directory = directory();
        if (directory.isEmpty() || !Files.isDirectory(directory.get())) {
            return List.of();
        }
        try (final var found = Files.walk(directory.get())) {
            return found.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(files.sqlFilesSuffix()))
                    .filter(path -> !UNDO_MIGRATION.matcher(path.getFileName().toString()).matches())
                    .sorted(APPLICATION_ORDER)
                    .flatMap(this::statementsIn)
                    .toList();
        } catch (final IOException _) {
            return List.of();
        }
    }

    /**
     * Where to look: a directory that was configured, or the statements themselves. Reading the
     * statements is what lets a project that keeps its `create table` next to its queries — which is
     * most of them — configure nothing at all.
     */
    private Optional<Path> directory() {
        return Optional.of(schema.sqlStatementsDirectory())
                .filter(configured -> !configured.isBlank())
                .map(Path::of)
                .or(() -> Optional.ofNullable(files.inputBaseDirectory()));
    }

    /**
     * @return the segments of the file's migration version, or null when its name is not one
     */
    private static List<BigInteger> version(final Path file) {
        final var name = VERSIONED_MIGRATION.matcher(file.getFileName().toString());
        if (!name.matches()) {
            return null;
        }
        // BigInteger rather than long because a version segment is whatever the project wrote, and
        // a timestamp version is already fourteen digits.
        return Stream.of(name.group(1).split("[._]")).map(BigInteger::new).toList();
    }

    /**
     * Segment by segment, and the shorter version first where one is the other's prefix: {@code V1}
     * comes before {@code V1.1}, as it does everywhere else these names are read.
     */
    private static int compareVersions(final List<BigInteger> left, final List<BigInteger> right) {
        for (var index = 0; index < Math.min(left.size(), right.size()); index++) {
            final var segment = left.get(index).compareTo(right.get(index));
            if (segment != 0) {
                return segment;
            }
        }
        return Integer.compare(left.size(), right.size());
    }

    /**
     * Cuts a file into statements at the separator, the way the statement parser cuts one.
     *
     * <p>Two things it does that splitting the raw text on the raw setting does not. The separator is
     * the text it says it is rather than a pattern, so the {@code |} the documentation offers as an
     * example does not match the empty string between every pair of characters. And the cuts are
     * found in {@link SqlText#maskLiteralsAndComments(String)} rather than in the DDL itself, so a
     * {@code ;} inside a string literal is not a cut — {@code default 'first; second'} would
     * otherwise leave two halves, neither of which parses, and the table they declare absent from the
     * catalog with nothing said. Which is the whole failure: the column turns up missing on some
     * statement that reads it, a long way from the DDL that lost it.</p>
     */
    private List<String> splitStatements(final String text) {
        final var separator = files.sqlStatementSeparator();
        if (separator == null || separator.isEmpty()) {
            return List.of(text);
        }
        final var masked = SqlText.maskLiteralsAndComments(text);
        final var statements = new ArrayList<String>();
        final var matcher = Pattern.compile(Pattern.quote(separator)).matcher(masked);
        var start = 0;
        while (matcher.find()) {
            statements.add(text.substring(start, matcher.start()));
            start = matcher.end();
        }
        statements.add(text.substring(start));
        return statements;
    }

    /**
     * Drops the byte order mark an editor may have written in front of the file.
     *
     * <p>Java's UTF-8 decoder hands the mark over as a character rather than eating it, so it lands
     * in front of the file's first statement — where it costs that one statement and nothing else.
     * The parser rejects it, and a statement this reader cannot parse is silently not in the
     * catalog, which is the right answer for a dialect nobody anticipated and the wrong one for an
     * {@code alter table} it understands perfectly. The result is a table that is present, correct
     * about every column but the one its file's first statement added, and a query using that
     * column reported as disagreeing with the schema.</p>
     */
    private static String withoutByteOrderMark(final String text) {
        return text.isEmpty() || text.charAt(0) != '﻿' ? text : text.substring(1);
    }

    /**
     * The database the whole schema is written for, for DDL that does not name one itself.
     *
     * <p>A file's own {@code -- vendor:} line still wins. This is for the schema nobody can edit —
     * a migration directory whose files are checksummed by the tool that applies them, where adding
     * a comment to a migration already run is not a change a project can make.</p>
     */
    private Optional<String> configuredVendor() {
        return Optional.of(schema.vendor()).filter(configured -> !configured.isBlank());
    }

    private Stream<Schemas.VendorStatement> statementsIn(final Path file) {
        final String text;
        try {
            text = withoutByteOrderMark(Files.readString(file, files.sqlFilesCharset()));
        } catch (final IOException _) {
            return Stream.empty();
        }
        final var statements = new ArrayList<Schemas.VendorStatement>();
        for (final var raw : splitStatements(BLOCK_COMMENT.matcher(text).replaceAll(""))) {
            if (raw.isBlank()) {
                continue;
            }
            final var vendor = VENDOR.matcher(raw);
            final var sql = FRONT_MATTER_LINE.matcher(raw).replaceAll("").strip();
            if (sql.isBlank()) {
                continue;
            }
            statements.add(new Schemas.VendorStatement(
                    vendor.find() ? Optional.of(vendor.group(1)) : configuredVendor(), sql));
        }
        return statements.stream();
    }

}
