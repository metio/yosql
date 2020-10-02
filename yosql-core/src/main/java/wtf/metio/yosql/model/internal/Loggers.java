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
 * Enumeration of known loggers.
 */
@LocaleData(@Locale("en"))
@BaseName("internal.loggers")
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
     * Logger for file readers.
     */
    READER,

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
    UTILITIES,

    /**
     * Logger for CLIs.
     */
    CLI,

}
