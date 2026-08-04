/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Object mother for {@link LoggingConfiguration}s.
 */
public final class LoggingConfigurations {

    public static LoggingConfiguration defaults() {
        return LoggingConfiguration.builder().build();
    }

    public static LoggingConfiguration using(final LoggingApis api) {
        return LoggingConfiguration.copyOf(defaults())
                .withApi(api);
    }

    /**
     * @return One configuration per {@link LoggingApis} value, so that a newly added API is covered by every test
     * that generates against all of them without anyone remembering to extend a list.
     */
    public static List<LoggingConfiguration> all() {
        return Arrays.stream(LoggingApis.values())
                .map(LoggingConfigurations::using)
                .toList();
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
