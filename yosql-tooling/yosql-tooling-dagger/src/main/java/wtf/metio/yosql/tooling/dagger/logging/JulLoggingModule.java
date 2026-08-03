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
import wtf.metio.yosql.codegen.logging.JulLoggingGenerator;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for java.util.logging based logging generators.
 */
@Module
public class JulLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.JUL)
    @Provides
    @Singleton
    LoggingGenerator provideJdkLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Fields fields) {
        return new JulLoggingGenerator(fields);
    }

}
