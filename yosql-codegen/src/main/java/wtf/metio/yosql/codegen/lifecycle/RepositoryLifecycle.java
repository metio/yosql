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

@BaseName("repository-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum RepositoryLifecycle {

    REPOSITORY_NAME_CALC_INPUT,
    REPOSITORY_NAME_CALC_SOURCE,
    REPOSITORY_NAME_CALC_RELATIVE,
    REPOSITORY_NAME_CALC_RAW,
    REPOSITORY_NAME_CALC_DOTTED,
    REPOSITORY_NAME_CALC_UPPER,
    REPOSITORY_NAME_CALC_QUALIFIED,
    REPOSITORY_NAME_RESULT,

    REPOSITORY_INTERFACE_CALC_SOURCE,
    REPOSITORY_INTERFACE_CALC_RAW,
    REPOSITORY_INTERFACE_CALC_PREFIXED,
    REPOSITORY_INTERFACE_CALC_SUFFIXED,
    REPOSITORY_INTERFACE_RESULT,

}
