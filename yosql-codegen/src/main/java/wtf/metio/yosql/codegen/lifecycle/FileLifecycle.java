/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("file-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
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
