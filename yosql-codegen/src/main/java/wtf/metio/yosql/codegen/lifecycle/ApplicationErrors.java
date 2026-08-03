/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("application-errors")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum ApplicationErrors {

    /**
     * Signals that a file could not be written.
     */
    FILE_WRITE_FAILED,

    /**
     * Signals that a file could not be parsed.
     */
    FILE_PARSING_FAILED,

    /**
     * Signals that reading files somehow failed.
     */
    READ_FILES_FAILED,

    /**
     * Signals that file parsing failed.
     */
    PARSE_FILES_FAILED,

    /**
     * Signals that code generation failed.
     */
    CODE_GENERATION_FAILED,

    /**
     * Signals that runtime environment is invalid.
     */
    RUNTIME_INVALID,

    /**
     * Signals that names configuration is invalid.
     */
    NAMES_CONFIG_INVALID,

    /**
     * Signals that no generator claims the configured logging API.
     */
    UNSUPPORTED_LOGGING_API,

}
