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
@BaseName("sql-configuration-lifecycle")
public enum SqlConfigurationLifecycle {

    BATCH_PREFIX_NAME_CHANGED,
    BATCH_SUFFIX_NAME_CHANGED,
    BLOCKING_PREFIX_NAME_CHANGED,
    BLOCKING_SUFFIX_NAME_CHANGED,
    MUTINY_PREFIX_NAME_CHANGED,
    MUTINY_SUFFIX_NAME_CHANGED,
    REACTOR_PREFIX_NAME_CHANGED,
    REACTOR_SUFFIX_NAME_CHANGED,
    RXJAVA_PREFIX_NAME_CHANGED,
    RXJAVA_SUFFIX_NAME_CHANGED,
    STREAM_LAZY_PREFIX_NAME_CHANGED,
    STREAM_LAZY_SUFFIX_NAME_CHANGED,
    STREAM_EAGER_PREFIX_NAME_CHANGED,
    STREAM_EAGER_SUFFIX_NAME_CHANGED

}
