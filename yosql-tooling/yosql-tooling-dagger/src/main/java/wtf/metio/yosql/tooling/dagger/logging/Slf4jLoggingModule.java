/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.tooling.dagger.logging.LoggingApiKey;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.logging.Slf4jLoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for slf4j based logging generators.
 */
@Module
public class Slf4jLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.SLF4J)
    @Provides
    @Singleton
    LoggingGenerator provideSlf4jLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Fields fields) {
        return new Slf4jLoggingGenerator(runtimeConfiguration.names(), fields);
    }

}
