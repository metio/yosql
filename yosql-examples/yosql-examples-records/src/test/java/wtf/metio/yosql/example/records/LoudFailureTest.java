/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.example.records;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.example.records.domain.OrderState;
import wtf.metio.yosql.example.records.persistence.EdgecaseRepository;

import javax.sql.DataSource;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The two ways a row mapper hands back a plausible wrong answer instead of failing.
 */
class LoudFailureTest {

    private static final UUID PRESENT = UUID.fromString("3a2b1c00-0000-4000-8000-000000000001");
    private static final UUID NULL_COUNT = UUID.fromString("3a2b1c00-0000-4000-8000-000000000002");
    private static final UUID UNKNOWN_STATE = UUID.fromString("3a2b1c00-0000-4000-8000-000000000003");

    private static EdgecaseRepository repository;

    @BeforeAll
    static void populate() {
        repository = new EdgecaseRepository(dataSource());
        repository.createEdgeCaseTable();
        repository.insertEdgeCase(PRESENT, 42L, "ACTIVE");
        repository.insertEdgeCase(NULL_COUNT, null, "ACTIVE");
        repository.insertEdgeCase(UNKNOWN_STATE, 1L, "PENDING_REVIEW");
    }

    @Test
    @DisplayName("a value that is there is read as itself")
    void readsAPresentValue() {
        assertEquals(42L, repository.findSample(PRESENT).orElseThrow().countValue());
        assertEquals(OrderState.ACTIVE, repository.findStateHolder(PRESENT).orElseThrow().state());
    }

    @Test
    @DisplayName("a NULL in a column a primitive reads fails instead of arriving as zero")
    void nullIntoPrimitiveFails() {
        final var failure = assertThrows(RuntimeException.class, () -> repository.findSample(NULL_COUNT));
        final var message = rootCause(failure).getMessage();
        assertTrue(message.contains("count_value"), message);
        assertTrue(message.contains("countValue"), message);
        assertTrue(message.contains("long"), message);
    }

    @Test
    @DisplayName("a stored value the enum does not know fails instead of being dropped")
    void unknownEnumValueFails() {
        final var failure = assertThrows(RuntimeException.class, () -> repository.findStateHolder(UNKNOWN_STATE));
        final var cause = rootCause(failure);
        assertTrue(cause instanceof IllegalArgumentException, cause.toString());
        assertTrue(cause.getMessage().contains("PENDING_REVIEW"), cause.getMessage());
        assertTrue(cause.getMessage().contains("OrderState"), cause.getMessage());
    }

    private static Throwable rootCause(final Throwable throwable) {
        var cause = throwable;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause;
    }

    private static DataSource dataSource() {
        final var dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:edgecases;DB_CLOSE_DELAY=-1");
        dataSource.setUser("sa");
        dataSource.setPassword("");
        return dataSource;
    }

}
