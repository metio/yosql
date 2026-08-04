/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.JavaFile;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import org.junit.jupiter.api.Assertions;
import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlParameter;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.nio.file.Path;
import java.util.List;

/**
 * Whatever a statement asks for, the generator answers with Java or with a diagnostic.
 *
 * <p>The compile oracle walks the combinations somebody enumerated. This one lets jqwik build the
 * statement instead — its type, what it returns, how it logs, whether it batches, how many
 * parameters it binds and what its SQL says — and holds the generator to the rule the project is
 * built on: a statement that cannot be generated correctly fails the build with a message naming it,
 * and one that can produces a repository that compiles. There is no third answer, and the failures
 * worth finding are the ones nobody thought to enumerate.</p>
 *
 * <p>A refusal counts as a pass, so long as it is one of ours. An exception from the JDK or from
 * JavaPoet is a failure however sensible its message, because it names neither the file nor the
 * statement — which is the whole reason the {@code exceptions} package exists.</p>
 */
class GeneratorFuzzTest {

    private static final String REPOSITORY = "com.example.persistence.DataRepository";

    /**
     * The converter every repository calls, compiled alongside so the test states what shape the
     * generated code expects of it.
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

    @Provide
    Arbitrary<String> parameterNames() {
        return Arbitraries.strings().withCharRange('a', 'f').ofMinLength(1).ofMaxLength(3);
    }

    @Provide
    Arbitrary<String> parameterTypes() {
        return Arbitraries.of("java.lang.String", "int", "long", "java.util.UUID",
                "java.time.Instant", "java.lang.Object", "java.util.List<java.lang.String>");
    }

    @Provide
    Arbitrary<List<SqlParameter>> parameters() {
        return Combinators.combine(parameterNames(), parameterTypes())
                .as((name, type) -> (SqlParameter) SqlParameter.builder()
                        .setName(name).setType(type).build())
                .list().ofMaxSize(4)
                // A name bound twice is one parameter, and the configurers upstream have already
                // merged them by the time a generator sees the statement.
                .map(list -> list.stream()
                        .collect(java.util.stream.Collectors.toMap(
                                parameter -> parameter.name().orElseThrow(),
                                parameter -> parameter,
                                (first, second) -> first,
                                java.util.LinkedHashMap::new))
                        .values().stream().toList());
    }

    @Provide
    Arbitrary<String> rawStatements() {
        return Arbitraries.of(
                        "select ", "* ", "from t ", "where ", "a = :a ", "b = ? ", "and ",
                        "in (:c) ", "'text' ", "-- note\n", "/* c */ ", ";", "\n", "x::text ",
                        "'it''s' ", "\"quoted\" ")
                .list().ofMinSize(1).ofMaxSize(12)
                .map(parts -> String.join("", parts));
    }

    @Provide
    Arbitrary<LoggingConfiguration> loggingApis() {
        return Arbitraries.of(
                LoggingConfigurations.defaults(), LoggingConfigurations.jul(),
                LoggingConfigurations.log4j(), LoggingConfigurations.slf4j(),
                LoggingConfigurations.system(), LoggingConfigurations.tiny());
    }

    @Property(tries = 400)
    void aStatementIsEitherGeneratedOrRefused(
            @ForAll final SqlStatementType type,
            @ForAll final ReturningMode returningMode,
            @ForAll final boolean createConnection,
            @ForAll final boolean catchAndRethrow,
            @ForAll final boolean usePreparedStatement,
            @ForAll final boolean executeBatch,
            @ForAll("parameters") final List<SqlParameter> parameters,
            @ForAll("rawStatements") final String rawStatement,
            @ForAll("loggingApis") final LoggingConfiguration logging) {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withType(type)
                .withReturningMode(returningMode)
                .withCreateConnection(createConnection)
                .withCatchAndRethrow(catchAndRethrow)
                .withUsePreparedStatement(usePreparedStatement)
                .withExecuteBatch(executeBatch)
                .withParameters(parameters);
        final List<SqlStatement> statements = List.of(SqlStatement.builder()
                .setSourcePath(Path.of("src", "main", "yosql", "queryData.sql"))
                .setConfiguration(configuration)
                .setRawStatement(rawStatement)
                .build());

        final String source;
        try {
            final var generated = DaoObjectMother
                    .repositoryGenerator(logging, RepositoriesConfigurations.defaults())
                    .generateRepositoryClass(REPOSITORY, statements);
            source = JavaFile.builder(generated.getPackageName(), generated.getType()).build().toString();
        } catch (final RuntimeException refused) {
            Assertions.assertTrue(isOurs(refused),
                    () -> "refused with " + refused.getClass().getName() + " rather than a diagnostic "
                            + "naming the statement, for " + describe(type, returningMode, rawStatement)
                            + "\n" + refused.getMessage());
            Assertions.assertNotNull(refused.getMessage(),
                    () -> refused.getClass().getSimpleName() + " carries no message, so the build "
                            + "would print null, for " + describe(type, returningMode, rawStatement));
            return;
        }

        final var errors = JavaCompilation.errorsIn(List.of(CONVERTER,
                new JavaCompilation.Source(REPOSITORY, source)));
        Assertions.assertTrue(errors.isEmpty(),
                () -> "javac rejected the repository generated for "
                        + describe(type, returningMode, rawStatement) + ":\n"
                        + String.join("\n", errors) + "\n\n" + source);
    }

    /**
     * Ours, meaning it comes from the package whose whole job is to say which statement went wrong.
     */
    private static boolean isOurs(final RuntimeException exception) {
        return exception.getClass().getPackageName()
                .equals("wtf.metio.yosql.codegen.exceptions");
    }

    private static String describe(
            final SqlStatementType type,
            final ReturningMode returningMode,
            final String rawStatement) {
        return type + " + " + returningMode + " over [" + rawStatement.replace("\n", "\\n") + "]";
    }

}
