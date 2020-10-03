/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.options;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * Options of JDBC names.
 */
@LocaleData(@Locale("en"))
@BaseName("options.jdbc-names")
public enum JdbcNamesOptions {

    DATA_SOURCE,

    CONNECTION,

    COLUMN_COUNT,

    COLUMN_LABEL,

    STATEMENT,

    META_DATA,

    RESULT_SET,

    BATCH,

    LIST,

    JDBC_INDEX,

    INDEX,

    ROW;

}
