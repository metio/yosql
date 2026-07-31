/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.lifecycle;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known file errors.
 */
@BaseName("file-errors")
@LocaleData({
        @Locale(value = "en", charset = "UTF-8"),
        @Locale(value = "de", charset = "UTF-8"),
})
public enum FileErrors {

    /**
     * Signals that we have no permission to read a file.
     */
    NO_READ_PERMISSION,

    /**
     * Signals that we have no permission to write a file.
     */
    NO_WRITE_PERMISSION,

    /**
     * Signals that something is not a directory.
     */
    NOT_A_DIRECTORY,

    /**
     * Signals that something does not exit.
     */
    NOT_EXISTS,

    /**
     * Signals that creating a directory failed.
     */
    DIRECTORY_CREATION_FAILED,

    /**
     * Signals that we cannot create a directory.
     */
    CANNOT_CREATE_DIRECTORY,

}
