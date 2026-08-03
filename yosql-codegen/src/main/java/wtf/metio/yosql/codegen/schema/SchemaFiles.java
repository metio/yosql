/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;

import java.io.IOException;
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
 * <p>Files are read in name order, which is what makes a directory of migrations —
 * {@code V1__create.sql}, {@code V2__add_column.sql} — describe the schema they leave behind rather
 * than an arbitrary one.</p>
 */
public final class SchemaFiles {

    private static final Pattern VENDOR = Pattern.compile(
            "^--\\s*vendor\\s*:\\s*(.+?)\\s*$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static final Pattern BLOCK_COMMENT = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
    private static final Pattern FRONT_MATTER_LINE = Pattern.compile("^\\s*--.*$", Pattern.MULTILINE);

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
                    .sorted(Comparator.comparing(Path::toString))
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
