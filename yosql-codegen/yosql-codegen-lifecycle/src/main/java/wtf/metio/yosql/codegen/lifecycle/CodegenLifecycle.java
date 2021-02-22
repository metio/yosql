/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@LocaleData({@Locale("en"), @Locale("de")})
@BaseName("codegen-lifecycle")
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
     * Signals that utilities are about to be generated.
     */
    GENERATE_UTILITIES,

}
