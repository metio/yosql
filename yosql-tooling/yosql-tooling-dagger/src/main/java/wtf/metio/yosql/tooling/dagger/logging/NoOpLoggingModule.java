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
import wtf.metio.yosql.codegen.logging.NoOpLoggingGenerator;

import javax.inject.Singleton;

/**
 * Dagger module for no-op logging generators.
 */
@Module
public class NoOpLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.NONE)
    @Provides
    @Singleton
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new NoOpLoggingGenerator();
    }

}
