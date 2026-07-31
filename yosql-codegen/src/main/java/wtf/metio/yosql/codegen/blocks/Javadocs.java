/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("javadocs")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum Javadocs {

    /**
     * Shows all files used for code generation.
     */
    USED_FILES,

    /**
     * Shows the single file that was used for code generation.
     */
    USED_FILE,

    /**
     * Shows all files used for a single generated method.
     */
    USED_FILES_METHOD,

    /**
     * Starts a list.
     */
    LIST_START,

    /**
     * Ends a list.
     */
    LIST_END,

    /**
     * Creates a simple list item which just contains a value.
     */
    LIST_ITEM,

    /**
     * Shows the executed statements.
     */
    EXECUTED_STATEMENTS,

    /**
     * Shows the executed statement.
     */
    EXECUTED_STATEMENT,

    /**
     * Shows the fallback statement.
     */
    FALLBACK,

    /**
     * Shows a vendor statement.
     */
    VENDOR,

    /**
     * Shows the SQL statement itself.
     */
    STATEMENT,

    /**
     * Shows the description of an SQL statement.
     */
    DESCRIPTION,

    /**
     * Shows how to disable generating a method.
     */
    DISABLE_WITH,

    /**
     * Shows all related types.
     */
    SEE

}

