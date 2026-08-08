/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

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
     * unaffected, since with no versions to order by this is name order throughout.</p>
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

    private Stream<Schemas.VendorStatement> statementsIn(final Path file) {
        final String text;
        try {
            text = Files.readString(file, files.sqlFilesCharset());
        } catch (final IOException _) {
            return Stream.empty();
        }
        final var statements = new ArrayList<Schemas.VendorStatement>();
        for (final var raw : BLOCK_COMMENT.matcher(text).replaceAll("").split(files.sqlStatementSeparator())) {
            if (raw.isBlank()) {
                continue;
            }
            final var vendor = VENDOR.matcher(raw);
            final var sql = FRONT_MATTER_LINE.matcher(raw).replaceAll("").strip();
            if (sql.isBlank()) {
                continue;
            }
            statements.add(new Schemas.VendorStatement(
                    vendor.find() ? Optional.of(vendor.group(1)) : Optional.empty(), sql));
        }
        return statements.stream();
    }

}
