/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.files;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.tooling.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.tooling.codegen.sql.ReturningMode;
import wtf.metio.yosql.tooling.codegen.sql.SqlType;
import wtf.metio.yosql.tooling.codegen.test.ObjectMother;

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
    void shouldProduceExpectedConfiguration() {
        // given
        final var source = Paths.get("Person/read.sql");
        final var yaml = "";
        final Map<String, List<Integer>> indices = Map.of();
        final var statementInFile = 1;

        // when
        final var configuration = factory().createConfiguration(source, yaml, indices, statementInFile);

        // then
        assertAll("Configuration",
                () -> assertTrue(configuration.hasRepository(), "hasRepository"),
                () -> assertFalse(configuration.hasParameters(), "hasParameters"),
                () -> assertFalse(configuration.isMethodBatchApi(), "isMethodBatchApi"),
                () -> assertTrue(configuration.isMethodCatchAndRethrow(), "isMethodCatchAndRethrow"),
                () -> assertFalse(configuration.isMethodRxJavaApi(), "isMethodRxJavaApi"),
                () -> assertTrue(configuration.isMethodStandardApi(), "isMethodStandardApi"),
                () -> assertFalse(configuration.isMethodStreamEagerApi(), "isMethodStreamEagerApi"),
                () -> assertFalse(configuration.isMethodStreamLazyApi(), "isMethodStreamLazyApi"),
                () -> assertEquals("read", configuration.getName(), "getName"),
                () -> assertEquals("readBatch", configuration.getBatchName(), "getBatchName"),
                () -> assertEquals("readFlow", configuration.getFlowableName(), "getFlowableName"),
                () -> assertEquals("readStreamEager", configuration.getStreamEagerName(), "getStreamEagerName"),
                () -> assertEquals("readStreamLazy", configuration.getStreamLazyName(), "getStreamLazyName"),
                () -> assertEquals("com.example.persistence.PersonRepository", configuration.getRepository(), "getRepository"),
                () -> assertEquals("Eager", configuration.getMethodEagerName(), "getMethodEagerName"),
                () -> assertEquals("Lazy", configuration.getMethodLazyName(), "getMethodLazyName"),
                () -> assertEquals("", configuration.getMethodBatchPrefix(), "getMethodBatchPrefix"),
                () -> assertEquals("Batch", configuration.getMethodBatchSuffix(), "getMethodBatchSuffix"),
                () -> assertEquals("", configuration.getMethodReactivePrefix(), "getMethodReactivePrefix"),
                () -> assertEquals("Flow", configuration.getMethodReactiveSuffix(), "getMethodReactiveSuffix"),
                () -> assertEquals("", configuration.getMethodStreamPrefix(), "getMethodStreamPrefix"),
                () -> assertEquals("Stream", configuration.getMethodStreamSuffix(), "getMethodStreamSuffix"),
                () -> assertNull(configuration.getVendor(), "getVendor"),
                () -> assertEquals(SqlType.READING, configuration.getType(), "getType"),
                () -> assertEquals(ReturningMode.LIST, configuration.getReturningMode(), "getReturningMode"),
                () -> assertFalse(configuration.shouldUsePluginBatchConfig(), "shouldUsePluginBatchConfig"),
                () -> assertFalse(configuration.shouldUsePluginStandardConfig(), "shouldUsePluginStandardConfig"),
                () -> assertFalse(configuration.shouldUsePluginStreamEagerConfig(), "shouldUsePluginStreamEagerConfig"),
                () -> assertFalse(configuration.shouldUsePluginStreamLazyConfig(), "shouldUsePluginStreamLazyConfig"),
                () -> assertFalse(configuration.shouldUsePluginCatchAndRethrowConfig(), "shouldUsePluginCatchAndRethrowConfig"),
                () -> assertFalse(configuration.shouldUsePluginRxJavaConfig(), "shouldUsePluginRxJavaConfig"));
    }

    private DefaultSqlConfigurationFactory factory() {
        return factory(RuntimeConfiguration.usingDefaults());
    }

    private DefaultSqlConfigurationFactory factory(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultSqlConfigurationFactory(
                ObjectMother.parserLocLogger(),
                runtimeConfiguration,
                new ExecutionErrors(),
                new MessageConveyor(Locale.ENGLISH));
    }

}