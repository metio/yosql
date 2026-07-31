/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import java.util.Locale;

/**
 * The rule that decides which column a record component reads.
 *
 * <p>A component's own name, read as {@code snake_case}: {@code tenantId} reads {@code tenant_id}.
 * That covers the overwhelming majority of a schema, and where it does not, the SQL says so —
 * {@code select amount_cents as minor_units} puts the mapping next to the query producing it,
 * rather than in configuration a reader of the query would have to go and find.</p>
 *
 * <p>Nesting adds no prefix. A nested record groups values on the Java side and the query knows
 * nothing about that grouping, so a component of a nested record claims the column matching its
 * own name exactly as a top-level one does.</p>
 */
public final class ColumnNames {

    public static String columnFor(final String componentName) {
        final var column = new StringBuilder(componentName.length() + 4);
        for (var index = 0; index < componentName.length(); index++) {
            final var character = componentName.charAt(index);
            if (Character.isUpperCase(character)) {
                if (index > 0 && needsSeparator(componentName, index)) {
                    column.append('_');
                }
                column.append(Character.toLowerCase(character));
            } else {
                column.append(character);
            }
        }
        return column.toString().toLowerCase(Locale.ROOT);
    }

    /**
     * A run of capitals is one word, so {@code taxId} becomes {@code tax_id} while {@code isVATRate}
     * becomes {@code is_vat_rate} rather than {@code is_v_a_t_rate}.
     */
    private static boolean needsSeparator(final String name, final int index) {
        final var previous = name.charAt(index - 1);
        if (!Character.isUpperCase(previous)) {
            return previous != '_';
        }
        final var next = index + 1 < name.length() ? name.charAt(index + 1) : '\0';
        return Character.isLowerCase(next);
    }

    private ColumnNames() {
        // constant class
    }

}
