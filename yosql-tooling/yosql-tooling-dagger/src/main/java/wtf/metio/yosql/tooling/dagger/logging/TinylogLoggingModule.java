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
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.logging.TinylogLoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for tinylog based logging generators.
 */
@Module
public class TinylogLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.TINYLOG)
    @Provides
    @Singleton
    LoggingGenerator provideJdkLoggingGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return new TinylogLoggingGenerator(runtimeConfiguration.names());
    }

}
