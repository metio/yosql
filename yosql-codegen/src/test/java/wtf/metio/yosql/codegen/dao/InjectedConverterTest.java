/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("a statement can ask for its converter to be handed in")
class InjectedConverterTest {

    private static SqlStatement statement(final SqlConfiguration configuration) {
        return SqlConfigurations.sqlStatement(configuration);
    }

    private static SqlConfiguration asking() {
        return SqlConfiguration.copyOf(SqlConfigurations.withCustomConverter()).withInjectConverter(true);
    }

    private static String constructorOf(final SqlStatement... statements) {
        return DaoObjectMother.constructorGenerator().repository(List.of(statements)).toString();
    }

    @Test
    @DisplayName("its repository takes it as a parameter instead of building it")
    void shouldInjectTheConverterItAsksFor() {
        final var constructor = constructorOf(statement(asking()));

        assertAll(
                () -> assertTrue(constructor.contains("this.item = item;"), constructor),
                () -> assertTrue(constructor.contains("ToItemConverter item"), constructor),
                () -> assertFalse(constructor.contains("new " + ConverterConfigurations.itemConverter()
                        .converterType().orElseThrow()), constructor));
    }

    @Test
    @DisplayName("a statement that does not ask still builds its own")
    void shouldNotInjectWhatNobodyAskedFor() {
        final var constructor = constructorOf(statement(SqlConfigurations.sqlConfiguration()));

        assertTrue(constructor.contains("new com.example.persistence.converter.ToMapConverter()"), constructor);
    }

    /**
     * The repositories of a project that does not use this are generated exactly as they were, which
     * is the whole point of asking per statement rather than per project.
     */
    @Test
    @DisplayName("a repository holding both takes only the one that asked")
    void shouldInjectOnlyTheConverterThatAsked() {
        final var constructor = constructorOf(
                statement(asking()),
                statement(SqlConfigurations.sqlConfiguration()));

        assertAll(
                () -> assertTrue(constructor.contains("this.item = item;"), constructor),
                () -> assertTrue(constructor.contains("new com.example.persistence.converter.ToMapConverter()"),
                        constructor));
    }

    /**
     * One field, initialised once: a converter that has to be handed in cannot also be built here.
     */
    @Test
    @DisplayName("statements sharing a converter share the answer, and one asking settles it")
    void shouldInjectAConverterOneStatementAsksFor() {
        final var constructor = constructorOf(
                statement(asking()),
                statement(SqlConfigurations.withCustomConverter()));

        assertAll(
                () -> assertTrue(constructor.contains("this.item = item;"), constructor),
                () -> assertFalse(constructor.contains("new " + ConverterConfigurations.itemConverter()
                        .converterType().orElseThrow()), constructor));
    }

    @Test
    @DisplayName("the project-wide setting still hands in everything")
    void shouldKeepInjectingEverythingWhenConfigured() {
        final var constructor = DaoObjectMother
                .constructorGenerator(RepositoriesConfigurations.injectConverters())
                .repository(List.of(statement(SqlConfigurations.sqlConfiguration())))
                .toString();

        assertFalse(constructor.contains("new com.example.persistence.converter.ToMapConverter()"), constructor);
    }

}
