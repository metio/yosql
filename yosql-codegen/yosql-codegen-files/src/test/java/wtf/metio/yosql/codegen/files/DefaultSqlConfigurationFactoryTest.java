/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

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
                () -> assertFalse(configuration.generateRxJavaApi(), "generateRxJavaApi"),
                () -> assertTrue(configuration.generateStandardApi(), "generateStandardApi"),
                () -> assertFalse(configuration.generateStreamEagerApi(), "generateStreamEagerApi"),
                () -> assertFalse(configuration.generateStreamLazyApi(), "generateStreamLazyApi"),
                () -> assertEquals("read", configuration.name(), "name"),
                () -> assertEquals("readBatch", configuration.batchName(), "batchName"),
                () -> assertEquals("readFlow", configuration.flowableName(), "flowableName"),
                () -> assertEquals("readStreamEager", configuration.streamEagerName(), "streamEagerName"),
                () -> assertEquals("readStreamLazy", configuration.streamLazyName(), "streamLazyName"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("Eager", configuration.eagerName(), "eagerName"),
                () -> assertEquals("Lazy", configuration.lazyName(), "lazyName"),
                () -> assertEquals("", configuration.batchPrefix(), "batchPrefix"),
                () -> assertEquals("Batch", configuration.batchSuffix(), "batchSuffix"),
                () -> assertEquals("", configuration.rxjava2Prefix(), "rxjava2Prefix"),
                () -> assertEquals("Flow", configuration.rxjava2Suffix(), "rxjava2Suffix"),
                () -> assertEquals("", configuration.streamPrefix(), "streamPrefix"),
                () -> assertEquals("Stream", configuration.streamSuffix(), "streamSuffix"),
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
                () -> assertFalse(configuration.generateBatchApi(), "generateBatchApi"),
                () -> assertTrue(configuration.catchAndRethrow(), "catchAndRethrow"),
                () -> assertFalse(configuration.generateRxJavaApi(), "generateRxJavaApi"),
                () -> assertTrue(configuration.generateStandardApi(), "generateStandardApi"),
                () -> assertFalse(configuration.generateStreamEagerApi(), "generateStreamEagerApi"),
                () -> assertFalse(configuration.generateStreamLazyApi(), "generateStreamLazyApi"),
                () -> assertEquals("insert", configuration.name(), "name"),
                () -> assertEquals("insertBatch", configuration.batchName(), "batchName"),
                () -> assertEquals("insertFlow", configuration.flowableName(), "flowableName"),
                () -> assertEquals("insertStreamEager", configuration.streamEagerName(), "streamEagerName"),
                () -> assertEquals("insertStreamLazy", configuration.streamLazyName(), "streamLazyName"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.repository(), "repository"),
                () -> assertEquals("Eager", configuration.eagerName(), "eagerName"),
                () -> assertEquals("Lazy", configuration.lazyName(), "lazyName"),
                () -> assertEquals("", configuration.batchPrefix(), "batchPrefix"),
                () -> assertEquals("Batch", configuration.batchSuffix(), "batchSuffix"),
                () -> assertEquals("", configuration.rxjava2Prefix(), "rxjava2Prefix"),
                () -> assertEquals("Flow", configuration.rxjava2Suffix(), "rxjava2Suffix"),
                () -> assertEquals("", configuration.streamPrefix(), "streamPrefix"),
                () -> assertEquals("Stream", configuration.streamSuffix(), "streamSuffix"),
                () -> assertEquals("", configuration.vendor(), "vendor"),
                () -> assertEquals(SqlType.WRITING, configuration.type(), "type"),
                () -> assertEquals(ReturningMode.ONE, configuration.returningMode(), "returningMode"));
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
                () -> assertEquals(ReturningMode.ONE, configuration.returningMode(), "returningMode"));
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