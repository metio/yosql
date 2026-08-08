/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.alter.AlterExpression;
import net.sf.jsqlparser.statement.alter.AlterOperation;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

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
        final var unfollowed = new LinkedHashMap<String, List<String>>();
        // Kept apart from `unfollowed` because there is no table to file them under: the statement
        // did not parse, so nothing here knows what it was about. They are matched to a table by
        // name when one is asked about, which is the most that can honestly be said of them.
        final var unparsed = new ArrayList<String>();
        for (final var statement : statements) {
            final var parsed = parse(statement);
            if (parsed.isEmpty()) {
                unparsed.add(statement);
                continue;
            }
            if (parsed.get() instanceof CreateTable created) {
                createTable(created).ifPresent(table ->
                        tables.put(Catalog.normalize(table.name()), table));
            } else if (parsed.get() instanceof Alter altered) {
                alterTable(altered, tables, (table, why) -> unfollowed
                        .computeIfAbsent(table, _ -> new ArrayList<>())
                        .add("%s — %s".formatted(oneLine(statement), why)));
            }
        }
        return Catalog.of(tables, unfollowed, unparsed.stream().map(CatalogReader::oneLine).toList());
    }

    /**
     * A statement as a diagnostic can print it: one line, and short enough to recognise the
     * migration it came from without reprinting the migration.
     */
    private static String oneLine(final String statement) {
        final var collapsed = statement.strip().replaceAll("\\s+", " ");
        return collapsed.length() <= 120 ? collapsed : collapsed.substring(0, 117) + "...";
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


    /**
     * Applies an {@code alter table} to what is already known about it.
     *
     * <p>A migration that this reader can follow keeps the catalog current. One it cannot follow
     * takes the table out of the catalog altogether, because a description known to be out of date is
     * worse than none: it fails statements that are correct against the real schema, while an absent
     * table only skips their checks.</p>
     */
    private static void alterTable(
            final Alter altered,
            final LinkedHashMap<String, Table> tables,
            final BiConsumer<String, String> unfollowed) {
        final var name = Catalog.normalize(altered.getTable().getName());
        var table = tables.get(name);
        if (table == null) {
            // Altering a table this reader never understood adds nothing it could be sure of.
            unfollowed.accept(name, "no create table for it had been read by then, so this was skipped");
            return;
        }
        for (final var expression : altered.getAlterExpressions()) {
            final var applied = apply(expression, table);
            if (applied.isEmpty()) {
                tables.remove(name);
                unfollowed.accept(name,
                        "this reader cannot say what it leaves behind, so the table was dropped from the catalog");
                return;
            }
            table = applied.get();
        }
        tables.put(name, table);
    }

    /**
     * @return the table as the expression leaves it, or empty when this reader cannot say
     */
    private static Optional<Table> apply(final AlterExpression expression, final Table table) {
        return switch (expression.getOperation()) {
            // `modify` restates a column in full, which is `with` replacing it in place.
            case ADD, MODIFY -> Optional.of(redefine(expression, table));
            case ALTER -> alterColumns(expression, table);
            case CHANGE -> change(expression, table);
            case DROP -> Optional.of(drop(expression, table));
            // The table lives on under a name this reader was not told, so nothing it holds about
            // the old one is true any more.
            case RENAME_TABLE -> Optional.empty();
            case RENAME -> rename(expression, table);
            // Constraints, indexes, comments and table options say nothing about the columns.
            default -> namesAColumn(expression) ? Optional.empty() : Optional.of(table);
        };
    }

    private static Table redefine(final AlterExpression expression, final Table table) {
        var altered = table;
        for (final var definition : columnsOf(expression)) {
            altered = altered.with(column(definition));
        }
        return altered;
    }

    /**
     * {@code alter column} is the one form whose spellings do not restate the column, so each is read
     * on its own terms: a new type keeps the nullability, and a nullability change keeps the type.
     */
    private static Optional<Table> alterColumns(final AlterExpression expression, final Table table) {
        var altered = table;
        for (final var definition : columnsOf(expression)) {
            final var existing = altered.column(definition.getColumnName());
            if (existing.isEmpty()) {
                return Optional.empty();
            }
            final var specs = String.join(" ", Optional.ofNullable(definition.getColumnSpecs()).orElse(List.of()))
                    .toLowerCase(Locale.ROOT);
            final var type = String.valueOf(definition.getColDataType());
            final var column = existing.get();
            if (specs.contains("drop not null")) {
                altered = altered.with(new Column(column.name(), column.sqlType(), true));
            } else if (specs.contains("not null")) {
                altered = altered.with(new Column(column.name(), column.sqlType(), false));
            } else if (definition.getColDataType() != null && specs.isBlank()) {
                altered = altered.with(new Column(column.name(), type, column.nullable()));
            } else {
                // A default, a collation, an identity — nothing this catalog holds, but nothing it
                // can be sure leaves the column alone either.
                return Optional.empty();
            }
        }
        return Optional.of(altered);
    }

    /**
     * MySQL's {@code change} renames a column and restates it in one go.
     */
    private static Optional<Table> change(final AlterExpression expression, final Table table) {
        final var oldName = expression.getColOldName();
        if (oldName == null) {
            return Optional.empty();
        }
        return Optional.of(redefine(expression, table.without(oldName)));
    }

    private static Optional<Table> rename(final AlterExpression expression, final Table table) {
        final var oldName = expression.getColOldName();
        final var newName = expression.getColumnName();
        return oldName == null || newName == null
                ? Optional.empty()
                : Optional.of(table.renaming(Catalog.unquote(oldName), Catalog.unquote(newName)));
    }

    private static Table drop(final AlterExpression expression, final Table table) {
        final var dropped = expression.getColumnName();
        // `drop constraint` and `drop index` name no column and leave the columns alone.
        return dropped == null ? table : table.without(Catalog.unquote(dropped));
    }

    private static List<AlterExpression.ColumnDataType> columnsOf(final AlterExpression expression) {
        return Optional.ofNullable(expression.getColDataTypeList()).orElse(List.of());
    }

    private static boolean namesAColumn(final AlterExpression expression) {
        return expression.getColumnName() != null
                || expression.getColOldName() != null
                || !columnsOf(expression).isEmpty();
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
        // Read as the tokens the parser produced rather than as one joined string. A quoted value is
        // a single token, so `comment 'set when not null elsewhere'` no longer reads as a NOT NULL
        // constraint and turn a nullable column into a primitive — which is the silent-zero-for-NULL
        // this class exists to prevent.
        return !says(columnSpecs, "not", "null") && !says(columnSpecs, "primary", "key");
    }

    private static boolean says(final List<String> columnSpecs, final String first, final String second) {
        for (var index = 0; index < columnSpecs.size() - 1; index++) {
            if (first.equalsIgnoreCase(columnSpecs.get(index))
                    && second.equalsIgnoreCase(columnSpecs.get(index + 1))) {
                return true;
            }
        }
        return false;
    }

}
