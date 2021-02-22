/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.errors;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Enumeration of known file errors.
 */
@LocaleData({@Locale("en"), @Locale("de")})
@BaseName("file-errors")
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
