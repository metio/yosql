package de.xn__ho_hia.yosql.model;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known application events.
 */
@LocaleData(@Locale("en"))
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
     * Signals the runtime of a single task.
     */
    TASK_RUNTIME,

    /**
     * Signals the runtime of the entire application.
     */
    APPLICATION_RUNTIME,

    /**
     * Signals that a file was parsed.
     */
    FILE_PARSED_FINISHED,

    /**
     * Signals that a file could not be parsed.
     */
    FILE_PARSED_FAILED,

    /**
     * Signals that a type was generated.
     */
    TYPE_GENERATED,

    /**
     * Signals that files are about to be read.
     */
    READ_FILES,

    /**
     * Signals that a file is considered for parsing.
     */
    CONSIDER_FILE,

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
    WRITE_FILES,

    /**
     * The name for the internal thread pool.
     */
    WORKER_POOL_NAME,

}
