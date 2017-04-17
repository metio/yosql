package de.xn__ho_hia.yosql.model;

import ch.qos.cal10n.BaseName;

/**
 * Enumeration of known application events.
 */
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
}
