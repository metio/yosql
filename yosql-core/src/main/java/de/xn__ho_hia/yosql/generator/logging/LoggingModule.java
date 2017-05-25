/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.logging;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.LoggingGenerator;
import de.xn__ho_hia.yosql.generator.logging.jdk.JDK;
import de.xn__ho_hia.yosql.generator.logging.jdk.JdkLoggingModule;
import de.xn__ho_hia.yosql.generator.logging.log4j.Log4j;
import de.xn__ho_hia.yosql.generator.logging.log4j.Log4jLoggingModule;
import de.xn__ho_hia.yosql.generator.logging.noop.NoOp;
import de.xn__ho_hia.yosql.generator.logging.noop.NoOpLoggingModule;
import de.xn__ho_hia.yosql.generator.logging.slf4j.Slf4j;
import de.xn__ho_hia.yosql.generator.logging.slf4j.Slf4jLoggingModule;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

/**
 * Dagger2 module for the logging API.
 */
@Module(includes = {
        JdkLoggingModule.class,
        Log4jLoggingModule.class,
        NoOpLoggingModule.class,
        Slf4jLoggingModule.class,
})
@SuppressWarnings("static-method")
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
