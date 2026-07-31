/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
