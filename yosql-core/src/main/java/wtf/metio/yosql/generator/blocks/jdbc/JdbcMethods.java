package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;

public interface JdbcMethods {

    JdbcDataSourceMethods dataSource();
    JdbcConnectionMethods connection();
    JdbcResultSetMethods resultSet();
    JdbcMetaDataMethods metaData();
    JdbcStatementMethods statement();

    interface JdbcDataSourceMethods {
        CodeBlock getConnection();
    }
    interface JdbcConnectionMethods {
        CodeBlock prepareStatement();
        CodeBlock prepareCallable();
    }
    interface JdbcResultSetMethods {
        CodeBlock getMetaData();
    }
    interface JdbcMetaDataMethods {
        CodeBlock getColumnCount();
    }
    interface JdbcStatementMethods {
        CodeBlock executeQuery();
        CodeBlock executeUpdate();
        CodeBlock executeBatch();
        CodeBlock addBatch();
    }

}
