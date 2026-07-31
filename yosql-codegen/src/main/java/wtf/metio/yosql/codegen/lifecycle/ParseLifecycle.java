/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("parse-lifecycle")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
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
