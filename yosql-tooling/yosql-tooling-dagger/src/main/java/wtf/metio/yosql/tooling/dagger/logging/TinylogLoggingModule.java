/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.codegen.logging.TinylogLoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for tinylog based logging generators.
 */
@Module
public class TinylogLoggingModule {

    @IntoSet
    @Provides
    @Singleton
    LoggingGenerator provideJdkLoggingGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return new TinylogLoggingGenerator(runtimeConfiguration.names());
    }

}
