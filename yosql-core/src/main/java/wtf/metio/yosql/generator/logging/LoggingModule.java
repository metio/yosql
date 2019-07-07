/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.dagger.Delegating;
import wtf.metio.yosql.model.ExecutionConfiguration;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.logging.jdk.JDK;
import wtf.metio.yosql.generator.logging.jdk.JdkLoggingModule;
import wtf.metio.yosql.generator.logging.log4j.Log4j;
import wtf.metio.yosql.generator.logging.log4j.Log4jLoggingModule;
import wtf.metio.yosql.generator.logging.noop.NoOp;
import wtf.metio.yosql.generator.logging.noop.NoOpLoggingModule;
import wtf.metio.yosql.generator.logging.slf4j.Slf4j;
import wtf.metio.yosql.generator.logging.slf4j.Slf4jLoggingModule;

/**
 * Dagger2 module for the logging API.
 */
@Module(includes = {
        JdkLoggingModule.class,
        Log4jLoggingModule.class,
        NoOpLoggingModule.class,
        Slf4jLoggingModule.class,
})
public class LoggingModule {

    @Delegating
    @Provides
    LoggingGenerator provideLoggingGenerator(
            final ExecutionConfiguration config,
            final @JDK LoggingGenerator jdkLoggingGenerator,
            final @Log4j LoggingGenerator log4jLoggingGenerator,
            final @NoOp LoggingGenerator noOpLoggingGenerator,
            final @Slf4j LoggingGenerator slf4jLoggingGenerator) {
        return new DelegatingLoggingGenerator(config, jdkLoggingGenerator, log4jLoggingGenerator, noOpLoggingGenerator,
                slf4jLoggingGenerator);
    }

}
