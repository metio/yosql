package de.xn__ho_hia.yosql.model;

/**
 * Enumeration of possible return modes.
 */
public enum ReturningMode {

    /**
     * Statement returns no data.
     */
    NONE,

    /**
     * Statement returns the first result.
     */
    SINGLE,

    /**
     * Statement returns the first result, fails if there are more than one.
     */
    FIRST,

    /**
     * Statement returns the entire result set.
     */
    LIST;

}
