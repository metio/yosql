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
     * Signals that not all converter aliases are unique.
     */
    DUPLICATE_CONVERTER_ALIAS_USED,

}
