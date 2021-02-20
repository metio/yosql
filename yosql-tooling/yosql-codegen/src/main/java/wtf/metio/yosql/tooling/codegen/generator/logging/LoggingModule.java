/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.logging;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.tooling.codegen.generator.api.LoggingGenerator;
import wtf.metio.yosql.tooling.codegen.generator.logging.jdk.JDK;
import wtf.metio.yosql.tooling.codegen.generator.logging.jdk.JdkLoggingModule;
import wtf.metio.yosql.tooling.codegen.generator.logging.log4j.Log4j;
import wtf.metio.yosql.tooling.codegen.generator.logging.log4j.Log4jLoggingModule;
import wtf.metio.yosql.tooling.codegen.generator.logging.noop.NoOp;
import wtf.metio.yosql.tooling.codegen.generator.logging.noop.NoOpLoggingModule;
import wtf.metio.yosql.tooling.codegen.generator.logging.slf4j.Slf4j;
import wtf.metio.yosql.tooling.codegen.generator.logging.slf4j.Slf4jLoggingModule;
import wtf.metio.yosql.tooling.codegen.model.annotations.Delegating;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;

/**
 * Dagger module for the logging API.
 */
@Module(includes = {
        JdkLoggingModule.class,
        Log4jLoggingModule.class,
        NoOpLoggingModule.class,
        Slf4jLoggingModule.class,
})
public final class LoggingModule {

    @Delegating
    @Provides
    LoggingGenerator provideLoggingGenerator(
            final RuntimeConfiguration runtime,
            final @JDK LoggingGenerator jdkLoggingGenerator,
            final @Log4j LoggingGenerator log4jLoggingGenerator,
            final @NoOp LoggingGenerator noOpLoggingGenerator,
            final @Slf4j LoggingGenerator slf4jLoggingGenerator) {
        return new DelegatingLoggingGenerator(
                runtime,
                jdkLoggingGenerator,
                log4jLoggingGenerator,
                noOpLoggingGenerator,
                slf4jLoggingGenerator);
    }

}
