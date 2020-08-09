package wtf.metio.yosql.generator.blocks.jdbc;

import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

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
    
    private JdbcObjectMother() {
        // factory class
    }

}
