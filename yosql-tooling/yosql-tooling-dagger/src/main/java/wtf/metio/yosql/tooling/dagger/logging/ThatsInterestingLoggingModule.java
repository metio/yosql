/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.logging.ThatsInterestingLoggingGenerator;

import javax.inject.Singleton;

/**
 * Dagger module for thats-interesting logging generators.
 */
@Module
public class ThatsInterestingLoggingModule {

    @IntoSet
    @Provides
    @Singleton
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new ThatsInterestingLoggingGenerator();
    }

}
