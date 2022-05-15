/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

/**
 * Options for the logging API used in the generated code.
 */
public enum LoggingApis {

    /**
     * Automatically select an appropriate implementation based on project metadata.
     */
    AUTO,

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
