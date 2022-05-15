/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.lifecycle.ExecutionErrors;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.testing.configs.RuntimeConfigurations;

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
        final var configuration = factory().createConfiguration(source, yaml, indices, statementInFile);

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
        final var configuration = factory().createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("read", configuration.name(), "name"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("", configuration.vendor(), "vendor"),
                () -> assertEquals(SqlType.READING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.MULTIPLE, configuration.returningMode(), "returningMode"));
    }

    @Test
    void shouldProduceExpectedWriteConfiguration() {
        // given
        final var source = Paths.get("Person/insert.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("insert", configuration.name(), "name"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("", configuration.vendor(), "vendor"),
                () -> assertEquals(SqlType.WRITING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.NONE, configuration.returningMode(), "returningMode"));
    }

    @Test
    void shouldProduceExpectedWriteConfigurationDrop() {
        // given
        final var source = Paths.get("Person/dropPersons.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("dropPersons", configuration.name(), "name"),
                () -> assertEquals(SqlType.WRITING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.NONE, configuration.returningMode(), "returningMode"));
    }

    @Test
    void shouldProduceExpectedReadConfigurationWithCustomRowConverter() {
        // given
        final var source = Paths.get("Item/findItem.sql");
        final var yaml = """
                name: findItemByName
                parameters:
                  - name: name
                    type: java.lang.String
                resultRowConverter:
                  alias: itemConverter""";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;
        final var config = RuntimeConfiguration.usingDefaults()
                .setConverter(ConverterConfiguration.usingDefaults()
                        .setRowConverters(List.of(ResultRowConverter.builder()
                                .setAlias("itemConverter")
                                .setConverterType("com.example.ItemConverter")
                                .setMethodName("asUserType")
                                .setResultType("com.example.Item")
                                .build()))
                        .build())
                .build();

        // when
        final var configuration = factory(config).createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("findItemByName", configuration.name(), "name"),
                () -> assertEquals(SqlType.READING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.MULTIPLE, configuration.returningMode(), "returningMode"),
                () -> assertTrue(configuration.resultRowConverter().isPresent(), "resultRowConverter"),
                () -> assertEquals("itemConverter", configuration.resultRowConverter().get().alias(), "alias"),
                () -> assertEquals("asUserType", configuration.resultRowConverter().get().methodName(), "methodName"),
                () -> assertEquals("com.example.ItemConverter", configuration.resultRowConverter().get().converterType(), "converterType"),
                () -> assertEquals("com.example.Item", configuration.resultRowConverter().get().resultType(), "resultType"));
    }

    @Test
    void shouldProduceExpectedReadConfigurationWithDefaultAndCustomRowConverter() {
        // given
        final var source = Paths.get("Item/findItem.sql");
        final var yaml = """
                name: findItemByName
                parameters:
                  - name: name
                    type: java.lang.String
                resultRowConverter:
                  alias: itemConverter""";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;
        final var config = RuntimeConfiguration.usingDefaults()
                .setConverter(ConverterConfiguration.usingDefaults()
                        .setDefaultConverter(ResultRowConverter.builder()
                                .setAlias("resultRow")
                                .setConverterType("com.example.ResultRowConverter")
                                .setMethodName("apply")
                                .setResultType("com.example.ResultRow")
                                .build())
                        .setRowConverters(List.of(ResultRowConverter.builder()
                                .setAlias("itemConverter")
                                .setConverterType("com.example.ItemConverter")
                                .setMethodName("asUserType")
                                .setResultType("com.example.Item")
                                .build()))
                        .build())
                .build();

        // when
        final var configuration = factory(config).createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertEquals("findItemByName", configuration.name(), "name"),
                () -> assertEquals(SqlType.READING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.MULTIPLE, configuration.returningMode(), "returningMode"),
                () -> assertTrue(configuration.resultRowConverter().isPresent(), "resultRowConverter"),
                () -> assertEquals("itemConverter", configuration.resultRowConverter().get().alias(), "alias"),
                () -> assertEquals("asUserType", configuration.resultRowConverter().get().methodName(), "methodName"),
                () -> assertEquals("com.example.ItemConverter", configuration.resultRowConverter().get().converterType(), "converterType"),
                () -> assertEquals("com.example.Item", configuration.resultRowConverter().get().resultType(), "resultType"));
    }

    private DefaultSqlConfigurationFactory factory() {
        return factory(RuntimeConfigurations.defaults());
    }

    private DefaultSqlConfigurationFactory factory(final RuntimeConfiguration runtimeConfiguration) {
        final var logger = LoggingObjectMother.logger();
        final var errors = new ExecutionErrors();
        final var messages = LoggingObjectMother.messages();
        return new DefaultSqlConfigurationFactory(
                logger,
                new DefaultSqlConfigurationParser(),
                new DefaultMethodSettingsConfigurer(runtimeConfiguration.repositories()),
                new DefaultMethodNameConfigurer(logger, runtimeConfiguration.repositories()),
                new DefaultMethodNameValidator(runtimeConfiguration.repositories(), errors, messages),
                new DefaultMethodApiConfigurer(runtimeConfiguration.repositories()),
                new DefaultMethodParameterConfigurer(logger, errors, messages),
                new DefaultMethodConverterConfigurer(runtimeConfiguration.converter()),
                new DefaultRepositoryNameConfigurer(logger, runtimeConfiguration.files(), runtimeConfiguration.repositories()));
    }

}
