/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The tables the build knows about, read from the project's own DDL.
 *
 * <p>Nothing here comes from a database. Generation has to work in a checkout with no services
 * running, which is why the catalog is read from `create table` statements the project already
 * keeps rather than from a connection.</p>
 *
 * <p>A catalog is deliberately partial. A table whose DDL the reader could not understand is absent
 * rather than empty, and the difference matters: absent means "nothing is known about this table",
 * which skips every check that would have used it. Empty would mean "this table has no columns",
 * which would fail all of them.</p>
 */
public final class Catalog {

    private final Map<String, Table> tables;

    private Catalog(final Map<String, Table> tables) {
        this.tables = tables;
    }

    public static Catalog of(final Map<String, Table> tables) {
        final var byLowerCaseName = new LinkedHashMap<String, Table>(tables.size());
        tables.forEach((name, table) -> byLowerCaseName.put(normalize(name), table));
        return new Catalog(Collections.unmodifiableMap(byLowerCaseName));
    }

    public static Catalog empty() {
        return new Catalog(Map.of());
    }

    /**
     * SQL identifiers are case-insensitive unless quoted, and projects disagree about which case to
     * write them in. Everything is compared lower-cased so that {@code TENANT}, {@code Tenant} and
     * {@code tenant} are one table.
     */
    public static String normalize(final String identifier) {
        return identifier.strip().toLowerCase(Locale.ROOT);
    }

    /**
     * @return the table, or empty when nothing is known about a table of that name
     */
    public Optional<Table> table(final String name) {
        return Optional.ofNullable(tables.get(normalize(name)));
    }

    public Set<String> tableNames() {
        return tables.keySet();
    }

    /**
     * The table a column named by a statement is declared in.
     *
     * <p>A qualifier settles it: {@code c.id} is the {@code customers} row's id whatever
     * {@code orders} happens to declare. Guessing instead — taking the first table in scope with a
     * column of that name — reads the wrong type for every self-join and every pair of tables
     * sharing a column name, which is most schemas.</p>
     *
     * <p>Without a qualifier the statement itself did not say, so the first table declaring the name
     * is the only answer available; SQL would have refused an ambiguous one.</p>
     *
     * @param column the column as the statement writes it, qualified or not
     * @param scope what the statement reads from
     */
    public Optional<Table> declaringTable(final String column, final TableScope scope) {
        final var dot = column.lastIndexOf('.');
        final var name = dot < 0 ? column : column.substring(dot + 1);
        if (dot >= 0) {
            return scope.resolve(column.substring(0, dot))
                    .flatMap(this::table)
                    .filter(table -> table.column(name).isPresent());
        }
        return scope.tables().stream()
                .map(this::table)
                .flatMap(Optional::stream)
                .filter(table -> table.column(name).isPresent())
                .findFirst();
    }

    public boolean isEmpty() {
        return tables.isEmpty();
    }

}
