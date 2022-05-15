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
@BaseName("repository-lifecycle")
public enum RepositoryLifecycle {

    REPOSITORY_NAME_CALC_INPUT,
    REPOSITORY_NAME_CALC_SOURCE,
    REPOSITORY_NAME_CALC_RELATIVE,
    REPOSITORY_NAME_CALC_RAW,
    REPOSITORY_NAME_CALC_DOTTED,
    REPOSITORY_NAME_CALC_UPPER,
    REPOSITORY_NAME_CALC_ACTUAL,
    REPOSITORY_NAME_CALC_NAME,

}
