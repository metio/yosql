/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("write-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum WriteLifecycle {

    /**
     * Signals that files are about to be written.
     */
    WRITE_FILES,

    /**
     * Signals that a file was written.
     */
    FILE_WRITE_FINISHED,

    /**
     * Signals that a file an earlier run wrote was removed.
     */
    FILE_DELETE_FINISHED,

}
