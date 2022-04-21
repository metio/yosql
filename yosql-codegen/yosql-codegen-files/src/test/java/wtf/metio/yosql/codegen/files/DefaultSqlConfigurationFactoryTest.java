/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
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
                () -> assertFalse(configuration.generateBatchApi(), "generateBatchApi"),
                () -> assertTrue(configuration.catchAndRethrow(), "catchAndRethrow"),
                () -> assertTrue(configuration.generateRxJavaApi(), "generateRxJavaApi"),
                () -> assertTrue(configuration.generateBlockingApi(), "generateBlockingApi"),
                () -> assertTrue(configuration.generateStreamEagerApi(), "generateStreamEagerApi"),
                () -> assertTrue(configuration.generateStreamLazyApi(), "generateStreamLazyApi"),
                () -> assertEquals("read", configuration.name(), "name"),
                () -> assertEquals("readBatch", configuration.batchName(), "batchName"),
                () -> assertEquals("readFlow", configuration.rxJavaName(), "rxJavaName"),
                () -> assertEquals("readStreamEager", configuration.streamEagerName(), "streamEagerName"),
                () -> assertEquals("readStreamLazy", configuration.streamLazyName(), "streamLazyName"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("", configuration.blockingPrefix(), "blockingPrefix"),
                () -> assertEquals("", configuration.blockingSuffix(), "blockingSuffix"),
                () -> assertEquals("", configuration.batchPrefix(), "batchPrefix"),
                () -> assertEquals("Batch", configuration.batchSuffix(), "batchSuffix"),
                () -> assertEquals("", configuration.rxjavaPrefix(), "rxjavaPrefix"),
                () -> assertEquals("Flow", configuration.rxjavaSuffix(), "rxjavaSuffix"),
                () -> assertEquals("", configuration.streamLazyPrefix(), "streamLazyPrefix"),
                () -> assertEquals("StreamLazy", configuration.streamLazySuffix(), "streamLazySuffix"),
                () -> assertEquals("", configuration.streamEagerPrefix(), "streamEagerPrefix"),
                () -> assertEquals("StreamEager", configuration.streamEagerSuffix(), "streamEagerSuffix"),
                () -> assertEquals("", configuration.vendor(), "vendor"),
                () -> assertEquals(SqlType.READING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.LIST, configuration.returningMode(), "returningMode"));
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
                () -> assertTrue(configuration.generateBatchApi(), "generateBatchApi"),
                () -> assertTrue(configuration.catchAndRethrow(), "catchAndRethrow"),
                () -> assertFalse(configuration.generateRxJavaApi(), "generateRxJavaApi"),
                () -> assertTrue(configuration.generateBlockingApi(), "generateBlockingApi"),
                () -> assertFalse(configuration.generateStreamEagerApi(), "generateStreamEagerApi"),
                () -> assertFalse(configuration.generateStreamLazyApi(), "generateStreamLazyApi"),
                () -> assertEquals("insert", configuration.name(), "name"),
                () -> assertEquals("insertBatch", configuration.batchName(), "batchName"),
                () -> assertEquals("insertFlow", configuration.rxJavaName(), "rxJavaName"),
                () -> assertEquals("insertStreamEager", configuration.streamEagerName(), "streamEagerName"),
                () -> assertEquals("insertStreamLazy", configuration.streamLazyName(), "streamLazyName"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("", configuration.blockingPrefix(), "blockingPrefix"),
                () -> assertEquals("", configuration.blockingSuffix(), "blockingSuffix"),
                () -> assertEquals("", configuration.batchPrefix(), "batchPrefix"),
                () -> assertEquals("Batch", configuration.batchSuffix(), "batchSuffix"),
                () -> assertEquals("", configuration.rxjavaPrefix(), "rxjavaPrefix"),
                () -> assertEquals("Flow", configuration.rxjavaSuffix(), "rxjavaSuffix"),
                () -> assertEquals("", configuration.streamLazyPrefix(), "streamLazyPrefix"),
                () -> assertEquals("StreamLazy", configuration.streamLazySuffix(), "streamLazySuffix"),
                () -> assertEquals("", configuration.streamEagerPrefix(), "streamEagerPrefix"),
                () -> assertEquals("StreamEager", configuration.streamEagerSuffix(), "streamEagerSuffix"),
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
                .setJdbc(JdbcConfiguration.usingDefaults()
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
                () -> assertEquals(ReturningMode.LIST, configuration.returningMode(), "returningMode"),
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
                .setJdbc(JdbcConfiguration.usingDefaults()
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
                () -> assertEquals(ReturningMode.LIST, configuration.returningMode(), "returningMode"),
                () -> assertTrue(configuration.resultRowConverter().isPresent(), "resultRowConverter"),
                () -> assertEquals("itemConverter", configuration.resultRowConverter().get().alias(), "alias"),
                () -> assertEquals("asUserType", configuration.resultRowConverter().get().methodName(), "methodName"),
                () -> assertEquals("com.example.ItemConverter", configuration.resultRowConverter().get().converterType(), "converterType"),
                () -> assertEquals("com.example.Item", configuration.resultRowConverter().get().resultType(), "resultType"));
    }

    private DefaultSqlConfigurationFactory factory() {
        return factory(RuntimeConfiguration.usingDefaults().build());
    }

    private DefaultSqlConfigurationFactory factory(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultSqlConfigurationFactory(
                new LocLoggerFactory(new MessageConveyor(Locale.ENGLISH)).getLocLogger("yosql.test"),
                runtimeConfiguration,
                new ExecutionErrors(),
                new MessageConveyor(Locale.ENGLISH));
    }

}