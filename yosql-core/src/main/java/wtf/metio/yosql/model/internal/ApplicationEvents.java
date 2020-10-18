/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.internal;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known application events.
 */
@LocaleData({@Locale("en"), @Locale("de")})
@BaseName("application-events")
public enum ApplicationEvents {

    /**
     * Signals that a file was written.
     */
    FILE_WRITE_FINISHED,

    /**
     * Signals that a file could not be written.
     */
    FILE_WRITE_FAILED,

    /**
     * Signals the options of a single task.
     */
    TASK_RUNTIME,

    /**
     * Signals the options of the entire application.
     */
    APPLICATION_RUNTIME,

    /**
     * Signals that a SQL statement is about to be parsed.
     */
    STATEMENT_PARSING_STARTING,

    /**
     * Signals that a SQL statement within a file was parsed.
     */
    STATEMENT_PARSED,

    /**
     * Signals that a YAML front-matter of a SQL statement was parsed.
     */
    STATEMENT_YAML_FRONT_MATTER_PARSED,

    /**
     * Signals that a SQL statement was parsed.
     */
    STATEMENT_PARSING_FINISHED,

    /**
     * Signals that a file could not be parsed.
     */
    FILE_PARSING_FAILED,

    /**
     * Signals that a type was generated.
     */
    TYPE_GENERATED,

    /**
     * Signals that files are about to be read.
     */
    READ_FILES,

    /**
     * Signals that reading files somehow failed.
     */
    READ_FILES_FAILED,

    /**
     * Signals that a file is considered for parsing.
     */
    CONSIDER_FILE,

    /**
     * Signals that a file or directory is encountered before parsing.
     */
    ENCOUNTER_FILE,

    /**
     * Signals that files are about to be parsed.
     */
    PARSE_FILES,

    /**
     * Signals that file parsing failed.
     */
    PARSE_FILES_FAILED,

    /**
     * Signals that code generation failed.
     */
    CODE_GENERATION_FAILED,

    /**
     * Signals that repositories are about to be generated.
     */
    GENERATE_REPOSITORIES,

    /**
     * Signals that utilities are about to be generated.
     */
    GENERATE_UTILITIES,

    /**
     * Signals that files are about to be written.
     */
    WRITE_FILES
}
