/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * What a select list names, told apart from what it reads.
 *
 * <p>The two are not the same thing, and conflating them is a mistake worth spelling out.
 * {@code select amount_cents as minor_units} <em>reads</em> the column {@code amount_cents} and
 * <em>names</em> the result {@code minor_units}. A check that the columns exist has to ask about the
 * first; matching a record's components to a row has to ask about the second. Checking an alias
 * against the table would report every aliased query as a mistake — and aliasing is what
 * {@link wtf.metio.yosql.codegen.records.SelectedColumns} exists to support.</p>
 *
 * <p>An item that reads no single column — a function call, a literal, arithmetic — has no source
 * column, and nothing is claimed about it.</p>
 */
public record SelectItems(List<Item> items) {

    /**
     * @param resultName   the name the result set carries, which is the alias when there is one
     * @param sourceColumn the column it reads, when the item is a plain column reference
     */
    public record Item(String resultName, Optional<String> sourceColumn) {
    }

    public SelectItems {
        items = List.copyOf(items);
    }

    /**
     * @return the select list, with a star expanded from the catalog, or empty when it cannot be
     *         enumerated
     */
    public static Optional<SelectItems> of(final String sql, final TableScope scope, final Catalog catalog) {
        final var parsed = parse(sql);
        if (parsed.isEmpty()) {
            // No parse means no select list anyone can reason about. A star still expands, because
            // the catalog says what it holds without anything having to read the query.
            return ExpandedColumns.of(sql, scope, catalog).map(SelectItems::fromNames);
        }
        final var items = new ArrayList<Item>();
        for (final var selectItem : parsed.get().getSelectItems()) {
            final var expression = selectItem.getExpression();
            final var alias = Optional.ofNullable(selectItem.getAlias()).map(it -> Catalog.unquote(it.getName()));
            if (expression instanceof Column column) {
                final var name = Catalog.unquote(column.getColumnName());
                items.add(new Item(alias.orElse(name), Optional.of(qualified(column, name))));
            } else if (alias.isPresent()) {
                // An expression with a name: the row carries it, but no column produced it.
                items.add(new Item(alias.get(), Optional.empty()));
            } else {
                // An unnamed expression — `count(*)`, a star. Whatever the row carries here cannot
                // be worked out from the query alone.
                return ExpandedColumns.of(sql, scope, catalog).map(SelectItems::fromNames);
            }
        }
        return items.isEmpty() ? Optional.empty() : Optional.of(new SelectItems(items));
    }

    private static String qualified(final Column column, final String name) {
        final var table = column.getTable();
        return table == null ? name : table.getName() + "." + name;
    }

    /**
     * Names with no expression behind them, which is what a star expanded from the catalog gives:
     * every one of those is a real column of a real table.
     */
    private static SelectItems fromNames(final List<String> names) {
        return new SelectItems(names.stream().map(name -> new Item(name, Optional.of(name))).toList());
    }

    private static Optional<PlainSelect> parse(final String sql) {
        try {
            return CCJSqlParserUtil.parse(sql) instanceof Select select && select instanceof PlainSelect plain
                    ? Optional.of(plain)
                    : Optional.empty();
        } catch (final Exception _) {
            return Optional.empty();
        }
    }

    /**
     * @return the columns a check for existence should ask about
     */
    public List<String> sourceColumns() {
        return items.stream().map(Item::sourceColumn).flatMap(Optional::stream).toList();
    }

    /**
     * @return the column behind a result name, when a single column produced it
     */
    public Optional<String> sourceOf(final String resultName) {
        return items.stream()
                .filter(item -> Catalog.normalize(item.resultName()).equals(Catalog.normalize(resultName)))
                .findFirst()
                .flatMap(Item::sourceColumn);
    }

    public List<String> resultNames() {
        return items.stream().map(Item::resultName).toList();
    }

}
