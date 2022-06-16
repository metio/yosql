/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

/**
 * Object mother for {@link LoggingConfiguration}s.
 */
public final class LoggingConfigurations {

    public static LoggingConfiguration defaults() {
        return LoggingConfiguration.builder().build();
    }

    public static LoggingConfiguration jul() {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(LoggingApis.JUL);
    }

    public static LoggingConfiguration log4j() {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(LoggingApis.LOG4J);
    }

    public static LoggingConfiguration slf4j() {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(LoggingApis.SLF4J);
    }

    public static LoggingConfiguration system() {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(LoggingApis.SYSTEM);
    }

    public static LoggingConfiguration tiny() {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(LoggingApis.TINYLOG);
    }

    private LoggingConfigurations() {
        // factory class
    }

}
