/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import wtf.metio.yosql.codegen.records.SelectedColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * What {@code select *} actually selects, once there is a catalog to ask.
 *
 * <p>Without one, a star names nothing the generator can see, so a statement using it can only
 * answer with a {@code Map} and cannot be checked against a record. With one, the columns are the
 * ones the tables declare, in the order they were declared — which is the order the database returns
 * them in — and everything that works for a written-out select list works for a star too.</p>
 *
 * <p>Only a star on its own is expanded, and only when every table in scope is one the catalog
 * knows. {@code select t.*, count(*)} mixes a star with something else, and a table the catalog
 * never saw could contribute any column; in both cases the answer stays "unknown", because a
 * half-expanded list would be worse than none.</p>
 */
public final class ExpandedColumns {

    private static final Pattern SELECT_STAR = Pattern.compile(
            "^\\s*select\\s+(?:distinct\\s+)?(?:([A-Za-z_][\\w$]*)\\s*\\.\\s*)?\\*\\s+from\\b",
            Pattern.CASE_INSENSITIVE);

    private ExpandedColumns() {
        // utility class, call #of() directly
    }

    /**
     * @return the columns the statement selects, expanding a star where the catalog allows it, or
     *         empty when the select list cannot be enumerated
     */
    public static Optional<List<String>> of(final String sql, final TableScope scope, final Catalog catalog) {
        final var written = SelectedColumns.of(sql);
        if (written.isPresent()) {
            return written;
        }
        if (!scope.exhaustive() || scope.tables().isEmpty()) {
            return Optional.empty();
        }
        final var star = SELECT_STAR.matcher(sql);
        if (!star.find()) {
            return Optional.empty();
        }
        final var qualifier = Optional.ofNullable(star.group(1));
        final var tables = qualifier
                .map(name -> scope.resolve(name).map(List::of).orElseGet(List::of))
                .orElseGet(scope::tables);
        if (tables.isEmpty()) {
            return Optional.empty();
        }
        final var columns = new ArrayList<String>();
        for (final var table : tables) {
            final var known = catalog.table(table);
            if (known.isEmpty()) {
                return Optional.empty();
            }
            columns.addAll(known.get().columnNames());
        }
        return columns.isEmpty() ? Optional.empty() : Optional.of(List.copyOf(columns));
    }

}
