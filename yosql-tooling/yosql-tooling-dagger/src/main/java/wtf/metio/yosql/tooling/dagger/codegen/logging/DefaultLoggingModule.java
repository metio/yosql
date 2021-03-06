/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.logging;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.logging.api.DelegatingLoggingGenerator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.logging.jul.JUL;
import wtf.metio.yosql.logging.log4j.Log4j;
import wtf.metio.yosql.logging.noop.NoOp;
import wtf.metio.yosql.logging.slf4j.Slf4j;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the logging API.
 */
@Module(includes = {
        JulLoggingModule.class,
        Log4jLoggingModule.class,
        NoOpLoggingModule.class,
        Slf4jLoggingModule.class,
})
public class DefaultLoggingModule {

    @Provides
    @Delegating
    public LoggingGenerator provideLoggingGenerator(
            final RuntimeConfiguration runtime,
            final @JUL LoggingGenerator julLoggingGenerator,
            final @Log4j LoggingGenerator log4jLoggingGenerator,
            final @Slf4j LoggingGenerator slf4jLoggingGenerator,
            final @NoOp LoggingGenerator noOpLoggingGenerator) {
        return new DelegatingLoggingGenerator(
                runtime.api(),
                julLoggingGenerator,
                log4jLoggingGenerator,
                slf4jLoggingGenerator,
                noOpLoggingGenerator);
    }

}
