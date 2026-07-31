/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.models.configuration;

/**
 * Options for the logging API used in the generated code.
 */
public enum LoggingApis {

    /**
     * Disables logging in any of the generated files.
     */
    NONE,

    /**
     * Uses java.util.logging in the generated code.
     */
    JUL,

    /**
     * Uses System.Logger in the generated code.
     */
    SYSTEM,

    /**
     * Uses log4j in the generated code.
     */
    LOG4J,

    /**
     * Uses slf4j in the generated code.
     */
    SLF4J,

    /**
     * Uses thats-interesting in the generated code.
     */
    TI,

    /**
     * Uses tinylog in the generated code.
     */
    TINYLOG

}
