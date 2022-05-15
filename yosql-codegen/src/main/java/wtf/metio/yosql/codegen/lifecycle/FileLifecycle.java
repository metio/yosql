/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@LocaleData({@Locale("en"), @Locale("de")})
@BaseName("file-lifecycle")
public enum FileLifecycle {

    /**
     * Signals that files are about to be read.
     */
    READ_FILES,

    /**
     * Signals that a file or directory is encountered before parsing.
     */
    ENCOUNTER_FILE,

    /**
     * Signals that a file is considered for parsing.
     */
    CONSIDER_FILE,

}
