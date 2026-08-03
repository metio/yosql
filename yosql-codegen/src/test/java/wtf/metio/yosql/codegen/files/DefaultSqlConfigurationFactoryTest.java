/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.RuntimeConfigurations;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultSqlConfigurationFactory")
class DefaultSqlConfigurationFactoryTest {

    @Test
    void shouldCreateConfiguration() {
        // given
        final var source = Paths.get("Person/read.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, "", indices, statementInFile);

        // then
        assertNotNull(configuration);
    }

    @Test
    void shouldProduceExpectedReadConfiguration() {
        // given
        final var source = Paths.get("Person/read.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, "", indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("read", configuration.name().get(), "name"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository().get(), "repository"),
                () -> assertEquals(SqlStatementType.READING, configuration.type().get(), "type"),
                () -> assertEquals(ReturningMode.MULTIPLE, configuration.returningMode().get(), "returningMode"));
    }

    @Test
    void shouldProduceExpectedWriteConfiguration() {
        // given
        final var source = Paths.get("Person/insert.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, "", indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("insert", configuration.name().get(), "name"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository().get(), "repository"),
                () -> assertEquals(SqlStatementType.WRITING, configuration.type().get(), "type"),
                () -> assertEquals(ReturningMode.NONE, configuration.returningMode().get(), "returningMode"));
    }

    @Test
    void shouldProduceExpectedWriteConfigurationDrop() {
        // given
        final var source = Paths.get("Person/dropPersons.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, "", indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("dropPersons", configuration.name().get(), "name"),
                () -> assertEquals(SqlStatementType.WRITING, configuration.type().get(), "type"),
                () -> assertEquals(ReturningMode.NONE, configuration.returningMode().get(), "returningMode"));
    }

    @Test
    @DisplayName("a statement that names no converter is generated against the build's default")
    void shouldProduceExpectedReadConfigurationWithDefaultRowConverter() {
        // given
        final var source = Paths.get("Item/findItem.sql");
        final var yaml = """
                name: findItemByName
                parameters:
                  - name: name
                    type: java.lang.String""";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;
        final var config = RuntimeConfiguration.builder()
                .setConverter(ConverterConfiguration.builder()
                        .setDefaultConverter(ResultRowConverter.builder()
                                .setAlias("resultRow")
                                .setConverterType("com.example.ResultRowConverter")
                                .setMethodName("apply")
                                .setResultType("com.example.ResultRow")
                                .build())
                        .build())
                .build();

        // when
        final var configuration = factory(config).createConfiguration(source, yaml, "", indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("findItemByName", configuration.name().get(), "name"),
                () -> assertEquals(SqlStatementType.READING, configuration.type().get(), "type"),
                () -> assertEquals(ReturningMode.MULTIPLE, configuration.returningMode().get(), "returningMode"),
                () -> assertTrue(configuration.resultRowConverter().isPresent(), "resultRowConverter"),
                () -> assertEquals("resultRow", configuration.resultRowConverter().get().alias().get(), "alias"),
                () -> assertEquals("apply", configuration.resultRowConverter().get().methodName().get(), "methodName"),
                () -> assertEquals("com.example.ResultRowConverter", configuration.resultRowConverter().get().converterType().get(), "converterType"),
                () -> assertEquals("com.example.ResultRow", configuration.resultRowConverter().get().resultType().get(), "resultType"));
    }

    private static SqlConfigurationFactory factory() {
        return factory(RuntimeConfigurations.defaults());
    }

    private static SqlConfigurationFactory factory(final RuntimeConfiguration runtimeConfiguration) {
        return FilesObjectMother.sqlConfigurationFactory(
                runtimeConfiguration.repositories(),
                runtimeConfiguration.converter());
    }

}
