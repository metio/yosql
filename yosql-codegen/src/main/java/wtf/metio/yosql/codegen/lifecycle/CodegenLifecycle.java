/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("codegen-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum CodegenLifecycle {

    /**
     * Signals that a type was generated.
     */
    TYPE_GENERATED,

    /**
     * Signals that repositories are about to be generated.
     */
    GENERATE_REPOSITORIES,

    /**
     * Signals that converters are about to be generated.
     */
    GENERATE_CONVERTERS,

}
