/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Combines the column overrides of two configurations of the same statement.
 *
 * <p>A statement's front matter is merged with the defaults it inherits, and the more specific one
 * wins per entry rather than wholesale — so naming one component's column does not discard the
 * others.</p>
 */
public final class ResultRowColumns {

    public static Optional<Map<String, String>> mergeColumns(
            final Optional<Map<String, String>> first,
            final Optional<Map<String, String>> second) {
        if (first.isEmpty()) {
            return second;
        }
        if (second.isEmpty()) {
            return first;
        }
        final var merged = new LinkedHashMap<>(second.get());
        merged.putAll(first.get());
        return Optional.of(Map.copyOf(merged));
    }

    private ResultRowColumns() {
        // factory class
    }

}
