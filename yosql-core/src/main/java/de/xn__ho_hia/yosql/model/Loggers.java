package de.xn__ho_hia.yosql.model;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known loggers.
 */
@LocaleData(@Locale("en"))
@BaseName("loggers")
public enum Loggers {

    /**
     * Logger for file writers.
     */
    WRITER,

    /**
     * Logger for file parsers.
     */
    PARSER,

    /**
     * Logger for file generators.
     */
    GENERATOR,

    /**
     * Logger for timers.
     */
    TIMER,

    /**
     * Logger for utilities.
     */
    UTILITIES;

}
