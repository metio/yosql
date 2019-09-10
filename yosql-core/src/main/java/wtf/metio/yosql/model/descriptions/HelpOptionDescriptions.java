/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.descriptions;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;
import wtf.metio.yosql.model.options.HelpOptions;

/**
 * Enumeration of known help option descriptions.
 */
@LocaleData(@Locale("en"))
@BaseName("help-option-descriptions")
public enum HelpOptionDescriptions {

    /**
     * Description for {@link HelpOptions#COMMAND}.
     */
    COMMAND_DESCRIPTION,

}
