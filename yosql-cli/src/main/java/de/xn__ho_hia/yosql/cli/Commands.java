package de.xn__ho_hia.yosql.cli;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of all known commands.
 */
@LocaleData(@Locale("en"))
@BaseName("commands")
public enum Commands {

    /**
     * The help command.
     */
    HELP,

    /**
     * The version command.
     */
    VERSION,

    /**
     * The generate command.
     */
    GENERATE,

}
