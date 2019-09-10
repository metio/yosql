package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.sql.SqlConfiguration;

public interface JdbcFields {

    String constantSqlStatementFieldName(SqlConfiguration configuration);
    String constantRawSqlStatementFieldName(SqlConfiguration configuration);
    String constantSqlStatementParameterIndexFieldName(SqlConfiguration configuration);

}
