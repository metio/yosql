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
import wtf.metio.yosql.codegen.logging.Log4jLoggingGenerator;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for log4j based logging generators.
 */
@Module
public class Log4jLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.LOG4J)
    @Provides
    @Singleton
    LoggingGenerator provideLog4jLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Fields fields) {
        return new Log4jLoggingGenerator(runtimeConfiguration.names(), fields);
    }

}
