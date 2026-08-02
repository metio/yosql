/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.MethodSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.sql.Connection;
import java.util.List;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("DefaultMethodsGenerator")
class DefaultMethodsGeneratorTest {

    private static MethodsGenerator generator(final RepositoriesConfiguration repositories) {
        return DaoObjectMother.delegatingMethodsGenerator(JavaConfigurations.defaults(), repositories);
    }

    private static List<MethodSpec> methodsOf(
            final RepositoriesConfiguration repositories,
            final List<SqlStatement> statements) {
        return StreamSupport.stream(generator(repositories).asMethods(statements).spliterator(), false)
                .filter(method -> !method.isConstructor())
                .toList();
    }

    private static boolean takesConnection(final MethodSpec method) {
        return !method.parameters().isEmpty()
                && method.parameters().getFirst().type().toString().equals(Connection.class.getName());
    }

    private static List<SqlStatement> statementWith(final boolean createConnection) {
        return List.of(SqlStatement.builder()
                .setSourcePath(java.nio.file.Path.of("src", "main", "yosql", "queryData.sql"))
                .setConfiguration(SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                        .withCreateConnection(createConnection))
                .setRawStatement("SELECT raw FROM table WHERE test = ? AND id = ?;")
                .build());
    }

    @Nested
    @DisplayName("with overloads generated")
    class Overloads {

        private final RepositoriesConfiguration repositories = RepositoriesConfigurations.defaults();

        @Test
        @DisplayName("a statement is reachable both with and without a connection")
        void shouldGenerateBothShapes() {
            final var methods = methodsOf(repositories, statementWith(true));

            assertAll(
                    () -> assertEquals(2, methods.size()),
                    () -> assertTrue(methods.stream().allMatch(method -> "queryData".equals(method.name()))),
                    () -> assertEquals(1, methods.stream().filter(DefaultMethodsGeneratorTest::takesConnection).count()),
                    () -> assertEquals(1, methods.stream().filter(method -> !takesConnection(method)).count()));
        }

        @Test
        @DisplayName("what createConnection asks for changes nothing, because both are generated")
        void shouldIgnoreCreateConnection() {
            final var opening = methodsOf(repositories, statementWith(true));
            final var given = methodsOf(repositories, statementWith(false));

            assertEquals(
                    opening.stream().map(MethodSpec::toString).sorted().toList(),
                    given.stream().map(MethodSpec::toString).sorted().toList());
        }

        @Test
        @DisplayName("the connection comes first, so the two overloads read as one signature")
        void shouldPutConnectionFirst() {
            final var withConnection = methodsOf(repositories, statementWith(true)).stream()
                    .filter(DefaultMethodsGeneratorTest::takesConnection)
                    .findFirst()
                    .orElseThrow();

            assertEquals("connection", withConnection.parameters().getFirst().name());
        }

    }

    @Nested
    @DisplayName("with overloads turned off")
    class WithoutOverloads {

        private final RepositoriesConfiguration repositories = RepositoriesConfigurations.withoutConnectionOverloads();

        @Test
        @DisplayName("a statement opening its own connection generates only that")
        void shouldGenerateOnlyTheOpeningShape() {
            final var methods = methodsOf(repositories, statementWith(true));

            assertAll(
                    () -> assertEquals(1, methods.size()),
                    () -> assertFalse(takesConnection(methods.getFirst())));
        }

        @Test
        @DisplayName("a statement taking a given connection generates only that")
        void shouldGenerateOnlyTheGivenShape() {
            final var methods = methodsOf(repositories, statementWith(false));

            assertAll(
                    () -> assertEquals(1, methods.size()),
                    () -> assertTrue(takesConnection(methods.getFirst())));
        }

    }

}
