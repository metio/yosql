package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

public final class JdbcObjectMother {

    public static JdbcNamesConfiguration jdbcNamesConfiguration() {
        return JdbcNamesConfiguration.builder()
                .setStatement("statement")
                .setRow("row")
                .setResultSet("resultSet")
                .setMetaData("metaData")
                .setList("list")
                .setJdbcIndex("jdbcIndex")
                .setIndex("index")
                .setDataSource("dataSource")
                .setConnection("connection")
                .setColumnLabel("columnLabel")
                .setColumnCount("columnCount")
                .setBatch("batch")
                .build();
    }

    public static JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods() {
        return new DefaultJdbcConnectionMethods(names(), jdbcNamesConfiguration());
    }
    
    public static JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods() {
        return new DefaultJdbcDataSourceMethods(jdbcNamesConfiguration());
    }

    public static JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods() {
        return new DefaultJdbcResultSetMethods(jdbcNamesConfiguration());
    }

    public static JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods() {
        return new DefaultJdbcMetaDataMethods(jdbcNamesConfiguration());
    }
    
    public static JdbcMethods.JdbcStatementMethods jdbcStatementMethods() {
        return new DefaultJdbcStatementMethods(jdbcNamesConfiguration());
    }

    private JdbcObjectMother() {
        // factory class
    }

}
