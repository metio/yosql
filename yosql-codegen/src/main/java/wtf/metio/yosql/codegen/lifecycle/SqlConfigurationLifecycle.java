/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("sql-configuration-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum SqlConfigurationLifecycle {

    EXECUTE_ONCE_PREFIX_CHANGED,
    EXECUTE_ONCE_SUFFIX_CHANGED,
    EXECUTE_BATCH_PREFIX_CHANGED,
    EXECUTE_BATCH_SUFFIX_CHANGED,
    EXECUTE_MANY_PREFIX_CHANGED,
    EXECUTE_MANY_SUFFIX_CHANGED,

}
