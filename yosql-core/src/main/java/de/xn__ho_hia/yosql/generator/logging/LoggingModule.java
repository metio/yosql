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
import de.xn__ho_hia.yosql.generator.helpers.TypicalFields;
import de.xn__ho_hia.yosql.generator.logging.jdk.JDK;
import de.xn__ho_hia.yosql.generator.logging.jdk.JdkLoggingGenerator;
import de.xn__ho_hia.yosql.generator.logging.log4j.Log4j;
import de.xn__ho_hia.yosql.generator.logging.log4j.Log4jLoggingGenerator;
import de.xn__ho_hia.yosql.generator.logging.noop.NoOp;
import de.xn__ho_hia.yosql.generator.logging.noop.NoOpLoggingGenerator;
import de.xn__ho_hia.yosql.generator.logging.slf4j.Slf4j;
import de.xn__ho_hia.yosql.generator.logging.slf4j.Slf4jLoggingGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

/**
 * Dagger2 module for the logging API.
 */
@Module
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

    @JDK
    @Provides
    LoggingGenerator provideJdkLoggingGenerator(final TypicalFields fields) {
        return new JdkLoggingGenerator(fields);
    }

    @Log4j
    @Provides
    LoggingGenerator provideLog4jLoggingGenerator(final TypicalFields fields) {
        return new Log4jLoggingGenerator(fields);
    }

    @NoOp
    @Provides
    LoggingGenerator provideNoOpLoggingGenerator() {
        return new NoOpLoggingGenerator();
    }

    @Slf4j
    @Provides
    LoggingGenerator provideSlf4jLoggingGenerator(final TypicalFields fields) {
        return new Slf4jLoggingGenerator(fields);
    }

}
