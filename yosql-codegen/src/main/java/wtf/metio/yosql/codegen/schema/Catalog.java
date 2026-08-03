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
    static String normalize(final String identifier) {
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

    public boolean isEmpty() {
        return tables.isEmpty();
    }

}
