package de.xn__ho_hia.yosql.model;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known help options.
 */
@LocaleData(@Locale("en"))
@BaseName("help-options")
public enum HelpOptions {

    /**
     * Option to specify the command of interest.
     */
    COMMAND,

}
