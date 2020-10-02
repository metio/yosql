/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.errors;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@LocaleData(@Locale("en"))
@BaseName("internal.file-errors")
public enum FileErrors {

    NO_READ_PERMISSION,
    NO_WRITE_PERMISSION,
    NOT_A_DIRECTORY,
    NOT_EXISTS,
    DIRECTORY_CREATION_FAILED,
    CANNOT_CREATE_DIRECTORY,

}
