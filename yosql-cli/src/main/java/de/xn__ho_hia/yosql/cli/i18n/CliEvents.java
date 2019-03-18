/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.cli.i18n;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Application level events in the CLI.
 */
@LocaleData(@Locale("en"))
@BaseName("cli-events")
@SuppressWarnings("javadoc")
public enum CliEvents {

    PROBLEM_DURING_OPTION_PARSING,

    HELP_REQUIRED,

    DETAIL_HELP_REQUIRED,

    UNKNOWN_COMMAND,

    HELP_FOR_VERSION,

    HELP_FOR_GENERATE,

    VERSION_INFORMATION_REQUESTED,

    UNKNOWN_EXCEPTION,

}
