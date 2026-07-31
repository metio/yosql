/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.orchestration;

/**
 * Enumeration of known loggers available during code generation.
 */
public enum Loggers {

    /**
     * Logger for file writers.
     */
    WRITER("yosql.writer"),

    /**
     * Logger for file parsers.
     */
    PARSER("yosql.parser"),

    /**
     * Logger for file readers.
     */
    READER("yosql.reader"),

    /**
     * Logger for repository code generators.
     */
    REPOSITORIES("yosql.repositories"),

    /**
     * Logger for timers.
     */
    TIMER("yosql.timer"),

    /**
     * Logger for converters.
     */
    CONVERTERS("yosql.converters"),

    /**
     * Logger for executions
     */
    EXECUTIONS("yosql.executions");

    public final String loggerName;

    Loggers(String loggerName) {
        this.loggerName = loggerName;
    }

}
