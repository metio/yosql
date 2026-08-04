/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * Reads a {@link Catalog} out of the DDL a project already keeps.
 *
 * <p>Nothing here connects to a database. Generation has to work in a checkout with no services
 * running, so the catalog comes from the {@code create table} statements the project already has.</p>
 *
 * <p>Parsing is JSqlParser's; the rule around it is
 * {@link wtf.metio.yosql.codegen.records.SelectedColumns}' — a statement it cannot parse leaves its
 * table absent from the catalog rather than failing anything. Absent means "nothing is known", which
 * skips every check that would have used it, and that is what keeps a schema written in a dialect
 * nobody anticipated from breaking a build.</p>
 *
 * <p>Statements apply in the order they are given, so an {@code alter table} extends the
 * {@code create table} before it. A directory of ordered migrations is readable as long as the
 * caller hands them over in order.</p>
 */
public final class CatalogReader {

    private CatalogReader() {
        // utility class, call #read() directly
    }

    /**
     * @param statements the raw SQL of every statement that might be DDL, in the order it applies
     * @return what could be read; a table the reader did not understand is simply not in it
     */
    public static Catalog read(final List<String> statements) {
        final var tables = new LinkedHashMap<String, Table>();
        for (final var statement : statements) {
            parse(statement).ifPresent(parsed -> {
                if (parsed instanceof CreateTable created) {
                    createTable(created).ifPresent(table ->
                            tables.put(Catalog.normalize(table.name()), table));
                } else if (parsed instanceof Alter altered) {
                    addColumns(altered, tables);
                }
            });
        }
        return Catalog.of(tables);
    }

    /**
     * @return the parsed statement, or empty for anything JSqlParser does not accept — a stored
     *         function whose body is another language, a dialect extension, a statement that is not
     *         SQL at all
     */
    private static Optional<Statement> parse(final String statement) {
        if (statement == null || statement.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(CCJSqlParserUtil.parse(statement));
        } catch (final Exception _) {
            return Optional.empty();
        }
    }

    private static Optional<Table> createTable(final CreateTable created) {
        final var definitions = created.getColumnDefinitions();
        if (definitions == null || definitions.isEmpty()) {
            // `create table ... as select ...` declares its columns somewhere this cannot see.
            return Optional.empty();
        }
        final var columns = new LinkedHashMap<String, Column>(definitions.size());
        for (final var definition : definitions) {
            final var column = column(definition);
            // Keyed the way the constraints below and the query side both spell it: a table-level
            // `primary key ("id")` has to find the column that `"id" bigint` declared.
            columns.put(Catalog.normalize(column.name()), column);
        }
        for (final var key : primaryKeyColumns(created)) {
            columns.computeIfPresent(key, (name, column) ->
                    new Column(column.name(), column.sqlType(), false));
        }
        return Optional.of(new Table(Catalog.unquote(created.getTable().getName()), columns));
    }

    /**
     * The columns of a table-level {@code primary key (...)} constraint.
     *
     * <p>A primary key can be written beside the column or beneath the whole table, and the second
     * spelling is the only one available for a composite key. Both mean the column cannot be null,
     * but only the first reaches the column's own specs — so reading the definitions alone made
     * {@code id bigint, primary key (id)} nullable while {@code id bigint primary key} was not, and
     * the generated record held a {@code Long} in one case and a {@code long} in the other.</p>
     */
    private static List<String> primaryKeyColumns(final CreateTable created) {
        final var indexes = created.getIndexes();
        if (indexes == null) {
            return List.of();
        }
        return indexes.stream()
                .filter(index -> index.getType() != null)
                .filter(index -> index.getType().toLowerCase(Locale.ROOT).contains("primary key"))
                .map(Index::getColumnsNames)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(Catalog::normalize)
                .toList();
    }


    private static void addColumns(final Alter altered, final LinkedHashMap<String, Table> tables) {
        final var name = Catalog.normalize(altered.getTable().getName());
        var table = tables.get(name);
        if (table == null) {
            // Altering a table this reader never understood adds nothing it could be sure of.
            return;
        }
        for (final var expression : altered.getAlterExpressions()) {
            if (expression.getOperation() != AlterOperation.ADD) {
                continue;
            }
            final var added = expression.getColDataTypeList();
            if (added == null) {
                continue;
            }
            for (final var definition : added) {
                table = table.with(column(definition));
            }
        }
        tables.put(name, table);
    }

    private static Column column(final ColumnDefinition definition) {
        return new Column(Catalog.unquote(definition.getColumnName()),
                String.valueOf(definition.getColDataType()),
                nullable(definition.getColumnSpecs()));
    }

    /**
     * A column is nullable unless its DDL says otherwise. {@code primary key} counts, because every
     * database refuses a null in one whether or not the column also says {@code not null}.
     */
    private static boolean nullable(final List<String> columnSpecs) {
        if (columnSpecs == null || columnSpecs.isEmpty()) {
            return true;
        }
        final var specs = String.join(" ", columnSpecs).toLowerCase(Locale.ROOT);
        return !specs.contains("not null") && !specs.contains("primary key");
    }

}
