/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ResultRowColumns")
class ResultRowColumnsTest {

    @Test
    @DisplayName("two absent maps stay absent")
    void bothAbsent() {
        assertTrue(ResultRowColumns.mergeColumns(Optional.empty(), Optional.empty()).isEmpty());
    }

    @Test
    @DisplayName("keeps the one that is present")
    void oneAbsent() {
        final var columns = Optional.of(Map.of("createdAt", "inserted_at"));
        assertEquals(columns, ResultRowColumns.mergeColumns(columns, Optional.empty()));
        assertEquals(columns, ResultRowColumns.mergeColumns(Optional.empty(), columns));
    }

    @Test
    @DisplayName("keeps entries from both")
    void union() {
        final var merged = ResultRowColumns.mergeColumns(
                Optional.of(Map.of("createdAt", "inserted_at")),
                Optional.of(Map.of("amount.minorUnits", "amount_cents"))).orElseThrow();
        assertEquals(2, merged.size());
        assertEquals("inserted_at", merged.get("createdAt"));
        assertEquals("amount_cents", merged.get("amount.minorUnits"));
    }

    @Test
    @DisplayName("the more specific configuration wins per entry, not wholesale")
    void firstWinsPerEntry() {
        final var merged = ResultRowColumns.mergeColumns(
                Optional.of(Map.of("createdAt", "inserted_at")),
                Optional.of(Map.of("createdAt", "added_at", "at", "created_at"))).orElseThrow();
        assertEquals("inserted_at", merged.get("createdAt"), "the statement's own entry wins");
        assertEquals("created_at", merged.get("at"), "and the entry it did not name survives");
    }

}
