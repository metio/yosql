/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.configuration.JdbcFieldsConfiguration;

final class DefaultJdbcFields implements JdbcFields {

    private static final String NAME_REGEX = "([a-z])([A-Z])";
    private static final String NAME_REPLACEMENT = "$1_$2";

    private final JdbcFieldsConfiguration options;

    DefaultJdbcFields(final JdbcFieldsConfiguration options) {
        this.options = options;
    }

    @Override
    public String constantSqlStatementFieldName(final SqlConfiguration configuration) {
        return configuration.getName()
                .replaceAll(NAME_REGEX, NAME_REPLACEMENT)
                .toUpperCase()
                + getVendor(configuration);
    }

    @Override
    public String constantRawSqlStatementFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + options.rawSuffix();
    }

    @Override
    public String constantSqlStatementParameterIndexFieldName(final SqlConfiguration configuration) {
        return constantSqlStatementFieldName(configuration) + options.indexSuffix();
    }

    private static String getVendor(final SqlConfiguration configuration) {
        return configuration.getVendor() == null
                ? ""
                : "_" + configuration.getVendor().replace(" ", "_").toUpperCase();
    }

}
