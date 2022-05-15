/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

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
        ThatsInterstingLoggingModule.class,
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
