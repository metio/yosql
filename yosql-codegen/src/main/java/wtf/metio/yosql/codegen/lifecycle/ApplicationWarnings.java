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
@BaseName("application-warnings")
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
