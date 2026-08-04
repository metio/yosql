/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A method returning a cursor hands its connection, statement and result set to the Stream, which
 * closes them once the caller is done. That holds only on the path that actually returns the Stream:
 * anything thrown between opening a resource and handing it over leaves it to the garbage collector,
 * and a pooled connection collected rather than returned is one the pool never gets back.
 *
 * <p>Read off the generated source rather than by running it, because what is being checked is that
 * every resource is opened inside a block that closes it — a property of the method's shape, which no
 * single execution demonstrates. The snapshots in the generator tests pin the exact output; this pins
 * why it looks that way.</p>
 */
@DisplayName("a cursor method")
final class CursorOwnershipTest {

    static Stream<Arguments> cursors() {
        final var arguments = new ArrayList<Arguments>();
        for (final var type : List.of(SqlStatementType.READING, SqlStatementType.WRITING, SqlStatementType.CALLING)) {
            for (final var createConnection : List.of(true, false)) {
                arguments.add(Arguments.of(
                        "%s connection=%s".formatted(type, createConnection ? "opened" : "given"),
                        type, createConnection));
            }
        }
        return arguments.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cursors")
    @DisplayName("closes what it opened when it never reaches the Stream")
    void shouldCloseEverythingItOpensOnTheThrowPath(
            final String description,
            final SqlStatementType type,
            final boolean createConnection) {
        final var method = cursorMethod(type, createConnection);
        // Once where the Stream gives them back, once where the method fails before handing them over.
        final var connectionCloses = createConnection ? 2 : 0;
        assertAll(description,
                () -> assertEquals(connectionCloses, occurrences(method, "connection.close();"),
                        () -> "a connection this method opened is closed on both paths, and one it was "
                                + "given on neither:\n" + method),
                () -> assertEquals(2, occurrences(method, "statement.close();"),
                        () -> "the statement outlives the method only through the Stream:\n" + method),
                () -> assertEquals(2, occurrences(method, "resultSet.close();"),
                        () -> "so does the result set:\n" + method),
                () -> assertTrue(method.contains("throw throwable;"),
                        () -> "the failure that caused the cleanup is what the caller sees:\n" + method),
                () -> assertTrue(method.contains("throwable.addSuppressed(suppressed);"),
                        () -> "a failure while closing must not replace it:\n" + method));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("cursors")
    @DisplayName("closes the rest even when closing one of them throws")
    void shouldNotLetOneCloseSkipTheOthers(
            final String description,
            final SqlStatementType type,
            final boolean createConnection) {
        final var closer = cursorMethod(type, createConnection);
        // Two resources to close leave one finally between them, three leave two.
        final var expected = createConnection ? 2 : 1;
        assertEquals(expected, occurrences(closer.substring(closer.indexOf("onClose(")), "} finally {"),
                () -> "each close belongs in the finally of the one before it, so that a driver "
                        + "throwing on an early close still returns the connection:\n" + closer);
    }

    private static int occurrences(final String source, final String needle) {
        var count = 0;
        var index = source.indexOf(needle);
        while (index >= 0) {
            count++;
            index = source.indexOf(needle, index + needle.length());
        }
        return count;
    }

    private static String cursorMethod(final SqlStatementType type, final boolean createConnection) {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(type)
                .withReturningMode(ReturningMode.CURSOR)
                .withUsePreparedStatement(true)
                .withCreateConnection(createConnection);
        final var statements = SqlConfigurations.sqlStatement();
        final var method = switch (type) {
            case READING -> DaoObjectMother.readMethodGenerator().readMethod(configuration, statements);
            case WRITING -> DaoObjectMother.writeMethodGenerator().writeMethod(configuration, statements);
            case CALLING -> DaoObjectMother.callMethodGenerator().callMethod(configuration, statements);
        };
        return method.toString();
    }

}
