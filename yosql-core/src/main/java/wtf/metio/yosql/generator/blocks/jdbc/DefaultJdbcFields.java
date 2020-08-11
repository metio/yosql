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
