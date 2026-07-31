/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known validation errors.
 */
@BaseName("validation-errors")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum ValidationErrors {

    /**
     * Signals that an invalid prefix was used for a query.
     */
    INVALID_PREFIX,

    /**
     * Signals that an unknown parameter was used.
     */
    UNKNOWN_PARAMETER,

}
