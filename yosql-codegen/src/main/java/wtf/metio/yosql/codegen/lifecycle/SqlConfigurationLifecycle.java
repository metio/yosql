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
