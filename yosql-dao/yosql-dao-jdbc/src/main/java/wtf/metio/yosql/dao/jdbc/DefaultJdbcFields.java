/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

public final class DefaultJdbcFields implements JdbcFields {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";

    private final JdbcConfiguration jdbc;

    public DefaultJdbcFields(final JdbcConfiguration jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.name()
                .replaceAll(NAME_REGEX, NAME_REPLACEMENT)
                .toUpperCase()
                + getVendor(configuration);
    }

    @Override
    public String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + jdbc.rawSuffix();
    }

    @Override
    public String constantSqlStatementParameterIndexFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + jdbc.indexSuffix();
    }

    private static String getVendor(final SqlConfiguration configuration) {
        return Strings.isBlank(configuration.vendor())
                ? ""
                : "_" + configuration.vendor().replace(" ", "_").toUpperCase();
    }

}
