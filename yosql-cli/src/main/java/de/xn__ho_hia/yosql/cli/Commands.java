/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
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
