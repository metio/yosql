/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * One table, and the columns the DDL declared for it.
 *
 * <p>Columns keep the order they were declared in, because that is the order {@code select *}
 * expands to and the order a reader of the DDL expects to see them in.</p>
 */
public record Table(String name, Map<String, Column> columns) {

    public Table {
        final var byLowerCaseName = new LinkedHashMap<String, Column>(columns.size());
        columns.forEach((column, definition) -> byLowerCaseName.put(Catalog.normalize(column), definition));
        // Map.copyOf does not keep insertion order, and the order columns were declared in is the
        // order `select *` expands to.
        columns = Collections.unmodifiableMap(byLowerCaseName);
    }

    public Optional<Column> column(final String columnName) {
        return Optional.ofNullable(columns.get(Catalog.normalize(columnName)));
    }

    public Set<String> columnNames() {
        return columns.keySet();
    }

    /**
     * @return this table with one more column, for an {@code alter table add column} applied after
     *         the {@code create table} that declared the rest
     */
    public Table with(final Column column) {
        final var extended = new LinkedHashMap<>(columns);
        extended.put(Catalog.normalize(column.name()), column);
        return new Table(name, extended);
    }

}
