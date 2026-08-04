/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.JavaFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * The repositories YoSQL writes are Java.
 *
 * <p>Every other test in this package compares generated code against a string, and that string was
 * copied from the generator's own output — so a generator that emits a method missing its closing
 * brace produces an expectation missing it too, and the suite agrees with itself forever. Four
 * returning modes shipped that way, and neither the snapshots nor the example projects could see it:
 * the examples only generate the combinations they happen to use.</p>
 *
 * <p>This asks javac instead. It has no opinion that can be blessed by re-recording, it reads the
 * whole file rather than the one method under test, and it covers combinations no example project
 * has a reason to contain.</p>
 */
@DisplayName("a generated repository")
class GeneratedRepositoryCompilesTest {

    private static final String REPOSITORY = "com.example.persistence.DataRepository";

    /**
     * The converter every generated repository calls, and the only type it needs that is neither in
     * the JDK nor generated beside it. Compiled with the repository rather than put on the class
     * path, so that the test says what shape the generated code expects of it.
     */
    private static final JavaCompilation.Source CONVERTER = new JavaCompilation.Source(
            "com.example.persistence.converter.ToMapConverter",
            """
            package com.example.persistence.converter;

            public final class ToMapConverter {
                public java.util.Map<java.lang.String, java.lang.Object> apply(
                        final java.sql.ResultSet resultSet) throws java.sql.SQLException {
                    return java.util.Map.of();
                }
            }
            """);

    static Stream<Arguments> configurations() {
        final var arguments = new ArrayList<Arguments>();
        for (final var type : SqlStatementType.values()) {
            for (final var returningMode : ReturningMode.values()) {
                for (final var logging : loggingApis()) {
                    for (final var createConnection : List.of(true, false)) {
                        for (final var catchAndRethrow : List.of(true, false)) {
                            arguments.add(Arguments.of(
                                    "%s %s logging=%s connection=%s catch=%s".formatted(type, returningMode,
                                            logging.api(), createConnection, catchAndRethrow),
                                    SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                                            .withType(type)
                                            .withReturningMode(returningMode)
                                            .withUsePreparedStatement(true)
                                            .withCreateConnection(createConnection)
                                            .withCatchAndRethrow(catchAndRethrow),
                                    logging));
                        }
                    }
                }
            }
        }
        return arguments.stream();
    }

    private static List<LoggingConfiguration> loggingApis() {
        final var apis = new ArrayList<LoggingConfiguration>();
        apis.add(LoggingConfigurations.defaults());
        apis.addAll(LoggingConfigurations.all());
        return apis;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configurations")
    @DisplayName("compiles")
    void compiles(
            final String description,
            final SqlConfiguration configuration,
            final LoggingConfiguration logging) {
        final var errors = JavaCompilation.errorsIn(List.of(CONVERTER, repository(configuration, logging)));
        Assertions.assertTrue(errors.isEmpty(),
                () -> "javac rejected the repository generated for " + description + ":\n"
                        + String.join("\n", errors) + "\n\n"
                        + repository(configuration, logging).code());
    }

    /**
     * The statement shapes that take a different path through the generator than a plain one: a
     * collection parameter assembles its query from parts rather than reading one constant, a batch
     * binds arrays, and an unprepared statement carries its query as text.
     */
    static Stream<Arguments> shapes() {
        final var plain = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration());
        final var shapes = List.of(
                Arguments.of("collection parameter",
                        plain.withParameters(SqlConfigurations.collectionParameter()),
                        "SELECT raw FROM table WHERE name in (:names);"),
                Arguments.of("batch",
                        plain.withExecuteBatch(true),
                        "SELECT raw FROM table WHERE test = ? AND id = ?;"),
                Arguments.of("unprepared statement without parameters",
                        plain.withParameters(List.of()).withUsePreparedStatement(false)
                                .withType(SqlStatementType.WRITING).withReturningMode(ReturningMode.NONE),
                        "DELETE FROM table;"),
                Arguments.of("statement holding a regular expression",
                        plain.withParameters(List.of()),
                        "SELECT raw FROM table WHERE digits ~ '\\d+' AND label = 'a''b';"));
        final var arguments = new ArrayList<Arguments>();
        for (final var shape : shapes) {
            for (final var logging : loggingApis()) {
                arguments.add(Arguments.of(shape.get()[0] + " logging=" + logging.api(),
                        shape.get()[1], shape.get()[2], logging));
            }
        }
        return arguments.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("shapes")
    @DisplayName("compiles whatever shape the statement takes")
    void compilesEveryShape(
            final String description,
            final SqlConfiguration configuration,
            final String rawStatement,
            final LoggingConfiguration logging) {
        final var source = repository(configuration, logging, rawStatement);
        final var errors = JavaCompilation.errorsIn(List.of(CONVERTER, source));
        Assertions.assertTrue(errors.isEmpty(),
                () -> "javac rejected the repository generated for " + description + ":\n"
                        + String.join("\n", errors) + "\n\n" + source.code());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("configurations")
    @DisplayName("declares an interface the repository itself satisfies")
    void interfaceMatchesImplementation(
            final String description,
            final SqlConfiguration configuration,
            final LoggingConfiguration logging) {
        final var repositories = RepositoriesConfigurations.defaults();
        final var generator = DaoObjectMother.repositoryGenerator(logging, repositories);
        final var statements = statements(SqlConfiguration.copyOf(configuration)
                .withRepositoryInterface(REPOSITORY + "Interface"));
        final var declaration = generator.generateRepositoryInterface(REPOSITORY + "Interface", statements);
        final var errors = JavaCompilation.errorsIn(List.of(CONVERTER,
                new JavaCompilation.Source(REPOSITORY + "Interface", asSource(declaration))));
        Assertions.assertTrue(errors.isEmpty(),
                () -> "javac rejected the interface generated for " + description + ":\n"
                        + String.join("\n", errors) + "\n\n" + asSource(declaration));
    }

    private static JavaCompilation.Source repository(
            final SqlConfiguration configuration,
            final LoggingConfiguration logging) {
        return repository(configuration, logging, "SELECT raw FROM table WHERE test = ? AND id = ?;");
    }

    private static JavaCompilation.Source repository(
            final SqlConfiguration configuration,
            final LoggingConfiguration logging,
            final String rawStatement) {
        final var generated = DaoObjectMother
                .repositoryGenerator(logging, RepositoriesConfigurations.defaults())
                .generateRepositoryClass(REPOSITORY, statements(configuration, rawStatement));
        return new JavaCompilation.Source(REPOSITORY, asSource(generated));
    }

    private static String asSource(final PackagedTypeSpec generated) {
        return JavaFile.builder(generated.getPackageName(), generated.getType()).build().toString();
    }

    private static List<SqlStatement> statements(final SqlConfiguration configuration) {
        return statements(configuration, "SELECT raw FROM table WHERE test = ? AND id = ?;");
    }

    private static List<SqlStatement> statements(
            final SqlConfiguration configuration,
            final String rawStatement) {
        return List.of(SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement(rawStatement)
                .build());
    }

}
