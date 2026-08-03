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
import wtf.metio.yosql.codegen.logging.ThatsInterestingLoggingGenerator;

import javax.inject.Singleton;

/**
 * Dagger module for thats-interesting logging generators.
 */
@Module
public class ThatsInterestingLoggingModule {

    @IntoMap
    @LoggingApiKey(LoggingApis.TI)
    @Provides
    @Singleton
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new ThatsInterestingLoggingGenerator();
    }

}
