/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.internals.testing.configs;

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
