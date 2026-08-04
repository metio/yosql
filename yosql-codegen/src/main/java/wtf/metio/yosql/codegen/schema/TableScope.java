/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The tables a statement can read columns from, and the names it calls them by.
 *
 * <p>{@code exhaustive} is the important part. It is true only when every source the statement
 * reads from is a plain table this could name — no subquery, no common table expression, no
 * function call. When it is false the statement can produce columns from somewhere the catalog
 * knows nothing about, so a column that appears in no known table is not evidence of a mistake, and
 * every check that would have used the scope is skipped.</p>
 *
 * <p>That is the same rule the rest of this package follows: an unknown is a reason to say nothing,
 * never a reason to fail a build.</p>
 */
public record TableScope(List<String> tables, Map<String, String> aliases, boolean exhaustive) {

    public TableScope {
        tables = List.copyOf(tables);
        aliases = Map.copyOf(aliases);
    }

    /** Nothing is known about what this statement reads from. */
    public static TableScope unknown() {
        return new TableScope(List.of(), Map.of(), false);
    }

    /**
     * @return what the statement reads from, or an unknown scope when it cannot be worked out
     */
    public static TableScope of(final String sql) {
        if (sql == null || sql.isBlank()) {
            return unknown();
        }
        try {
            return of(CCJSqlParserUtil.parse(sql));
        } catch (final Exception _) {
            return unknown();
        }
    }

    private static TableScope of(final Statement statement) {
        return switch (statement) {
            case Select select -> select(select);
            case Update update -> update(update);
            case Insert insert -> single(insert.getTable());
            case Delete delete -> delete(delete);
            default -> unknown();
        };
    }

    /**
     * {@code update t set ... from other} and its MySQL multi-table spelling read from more than the
     * table they write to, and a parameter checked against the wrong table takes its type from the
     * wrong column. The extra sources count, or the scope is not exhaustive.
     */
    private static TableScope update(final Update update) {
        final var sources = new ArrayList<FromItem>();
        sources.add(update.getTable());
        if (update.getFromItem() != null) {
            sources.add(update.getFromItem());
        }
        addJoined(sources, update.getStartJoins());
        addJoined(sources, update.getJoins());
        return scopeOf(sources);
    }

    /**
     * {@code delete from t using other} and {@code delete t from t join other} the same way.
     */
    private static TableScope delete(final Delete delete) {
        final var sources = new ArrayList<FromItem>();
        sources.add(delete.getTable());
        if (delete.getUsingList() != null) {
            sources.addAll(delete.getUsingList());
        }
        addJoined(sources, delete.getJoins());
        return scopeOf(sources);
    }

    private static void addJoined(final List<FromItem> sources, final List<Join> joins) {
        if (joins != null) {
            joins.forEach(join -> sources.add(join.getRightItem()));
        }
    }

    private static TableScope select(final Select select) {
        if (!(select instanceof PlainSelect plain)) {
            // A set operation — union, intersect — reads from several selects at once.
            return unknown();
        }
        if (plain.getWithItemsList() != null && !plain.getWithItemsList().isEmpty()) {
            // A common table expression names columns the catalog has never seen.
            return unknown();
        }
        final var sources = new ArrayList<FromItem>();
        if (plain.getFromItem() != null) {
            sources.add(plain.getFromItem());
        }
        addJoined(sources, plain.getJoins());
        return scopeOf(sources);
    }

    /**
     * @return the scope those sources make up, or unknown when any of them is not a plain table — so
     *         that a scope either names everything the statement reads or admits that it does not
     */
    private static TableScope scopeOf(final List<FromItem> sources) {
        if (sources.isEmpty()) {
            return unknown();
        }
        final var tables = new ArrayList<String>(sources.size());
        final var aliases = new LinkedHashMap<String, String>();
        for (final var source : sources) {
            if (!(source instanceof Table table)) {
                // A subquery or a function can produce columns nothing here could check.
                return unknown();
            }
            final var name = table.getName();
            tables.add(name);
            Optional.ofNullable(table.getAlias())
                    .map(alias -> alias.getName())
                    .ifPresent(alias -> aliases.put(Catalog.normalize(alias), name));
        }
        return new TableScope(tables, aliases, true);
    }

    private static TableScope single(final Table table) {
        if (table == null) {
            return unknown();
        }
        final var aliases = new LinkedHashMap<String, String>();
        Optional.ofNullable(table.getAlias())
                .map(alias -> alias.getName())
                .ifPresent(alias -> aliases.put(Catalog.normalize(alias), table.getName()));
        return new TableScope(List.of(table.getName()), aliases, true);
    }

    /**
     * Resolves the part before the dot in {@code t.slug} to the table it names, which is either an
     * alias the statement declared or the table's own name.
     *
     * @return the table that qualifier refers to, or empty when the statement never mentioned it
     */
    public Optional<String> resolve(final String qualifier) {
        final var normalized = Catalog.normalize(qualifier);
        final var aliased = aliases.get(normalized);
        if (aliased != null) {
            return Optional.of(aliased);
        }
        return tables.stream()
                .filter(table -> Catalog.normalize(table).equals(normalized))
                .findFirst();
    }

}
