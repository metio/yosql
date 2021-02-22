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
@BaseName("parse-lifecycle")
public enum ParseLifecycle {

    /**
     * Signals that files are about to be parsed.
     */
    PARSE_FILES,

    /**
     * Signals that a SQL statement is about to be parsed.
     */
    STATEMENT_PARSING_STARTING,

    /**
     * Signals that a SQL statement within a file was parsed.
     */
    STATEMENT_PARSED,

    /**
     * Signals that a YAML front-matter of a SQL statement was parsed.
     */
    STATEMENT_YAML_FRONT_MATTER_PARSED,

    /**
     * Signals that a SQL statement was parsed.
     */
    STATEMENT_PARSING_FINISHED,

}
