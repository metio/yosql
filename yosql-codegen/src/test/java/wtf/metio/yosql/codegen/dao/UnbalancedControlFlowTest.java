/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * A statement that neither opens its own connection nor catches and rethrows opens one control flow
 * fewer than the others.
 *
 * <p>Closing a fixed number regardless does not produce slightly wrong Java — JavaPoet refuses with
 * "cannot unindent 1 from 0", which names neither the statement nor the setting that caused it. The
 * combination is reachable from configuration alone, and reachable for every statement in a project
 * once connection overloads generate a variant that takes its connection from the caller.</p>
 */
@DisplayName("control flows opened and closed")
class UnbalancedControlFlowTest {

    private static SqlConfiguration takesAConnectionAndDoesNotCatch(final ReturningMode mode) {
        return SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false)
                .withCatchAndRethrow(false)
                .withReturningMode(mode);
    }

    private static List<SqlStatement> statement(final SqlConfiguration configuration) {
        return List.of(SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement("SELECT raw FROM table WHERE test = ? AND id = ?;")
                .build());
    }

    @Test
    @DisplayName("a read closes exactly what it opened")
    void shouldBalanceReads() {
        final var generator = DaoObjectMother.readMethodGenerator();

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.SINGLE);
                    generator.readMethod(configuration, statement(configuration));
                }, "returning a single row"),
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.MULTIPLE);
                    generator.readMethod(configuration, statement(configuration));
                }, "returning many rows"));
    }

    @Test
    @DisplayName("a write closes exactly what it opened")
    void shouldBalanceWrites() {
        final var generator = DaoObjectMother.writeMethodGenerator();

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.NONE);
                    generator.writeMethod(configuration, statement(configuration));
                }, "returning nothing"),
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.SINGLE);
                    generator.writeMethod(configuration, statement(configuration));
                }, "returning a single row"),
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.NONE);
                    generator.batchWriteMethod(configuration, statement(configuration));
                }, "as a batch"));
    }

    @Test
    @DisplayName("a stored procedure call closes exactly what it opened")
    void shouldBalanceCalls() {
        final var generator = DaoObjectMother.callMethodGenerator();

        assertAll(
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.SINGLE);
                    generator.callMethod(configuration, statement(configuration));
                }, "returning a single row"),
                () -> assertDoesNotThrow(() -> {
                    final var configuration = takesAConnectionAndDoesNotCatch(ReturningMode.MULTIPLE);
                    generator.callMethod(configuration, statement(configuration));
                }, "returning many rows"));
    }

}
