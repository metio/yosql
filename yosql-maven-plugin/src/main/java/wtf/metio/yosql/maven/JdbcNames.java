package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;

/**
 * Configures names used during code generation with the JDBC API.
 */
public class JdbcNames {

    JdbcNamesConfiguration asConfiguration() {
        return JdbcNamesConfiguration.builder()
                .setBatch("batch") // TODO: configure w/ Maven
                .setColumnCount("columnCount") // TODO: configure w/ Maven
                .setColumnLabel("columnLabel") // TODO: configure w/ Maven
                .setConnection("connection") // TODO: configure w/ Maven
                .setDataSource("dataSource") // TODO: configure w/ Maven
                .setIndex("index") // TODO: configure w/ Maven
                .setJdbcIndex("jdbcIndex") // TODO: configure w/ Maven
                .setList("list") // TODO: configure w/ Maven
                .setMetaData("metaData") // TODO: configure w/ Maven
                .setResultSet("resultSet") // TODO: configure w/ Maven
                .setRow("row") // TODO: configure w/ Maven
                .setStatement("statement") // TODO: configure w/ Maven
                .build();
    }

}
