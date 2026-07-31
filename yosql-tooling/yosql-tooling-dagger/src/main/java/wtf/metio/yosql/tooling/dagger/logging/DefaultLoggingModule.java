/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.tooling.dagger.logging;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.logging.DelegatingLoggingGenerator;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.Delegating;

import javax.inject.Singleton;
import java.util.Set;

/**
 * Dagger module for the logging API.
 */
@Module(includes = {
        JulLoggingModule.class,
        Log4jLoggingModule.class,
        NoOpLoggingModule.class,
        Slf4jLoggingModule.class,
        TinylogLoggingModule.class,
        SystemLoggingModule.class,
        ThatsInterestingLoggingModule.class,
})
public class DefaultLoggingModule {

    @Provides
    @Delegating
    @Singleton
    LoggingGenerator provideLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Set<LoggingGenerator> loggingGenerators) {
        return new DelegatingLoggingGenerator(runtimeConfiguration.logging(), loggingGenerators);
    }

}
