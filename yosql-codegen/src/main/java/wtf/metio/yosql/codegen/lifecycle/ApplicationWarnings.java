/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("application-warnings")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum ApplicationWarnings {

    /**
     * Signals that an SQL statement was ignored during code generation.
     */
    SQL_STATEMENT_IGNORED,

    /**
     * Signals that an annotation type cannot be guessed.
     */
    CANNOT_GUESS_ANNOTATION_TYPE,

    /**
     * Signals that an annotation type cannot be guessed.
     */
    CANNOT_GUESS_ANNOTATION_MEMBER_TYPE,

}
