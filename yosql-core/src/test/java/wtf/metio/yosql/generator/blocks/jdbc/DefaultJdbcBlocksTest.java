/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.blocks.jdbc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlParameter;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.*;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.*;
import static wtf.metio.yosql.generator.logging.LoggingObjectMother.loggingGenerator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.runtimeConfiguration;

@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    private DefaultJdbcBlocks generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultJdbcBlocks(
                runtimeConfiguration(),
                genericBlocks(),
                controlFlows(),
                names(),
                variables(),
                jdbcNames(),
                jdbcFields(),
                jdbcMethods(),
                loggingGenerator());
    }

    @Test
    void connectionVariable() {
        Assertions.assertEquals("""
                final java.sql.Connection connection = dataSource.getConnection()""", generator.connectionVariable().toString());
    }

    @Test
    void statementVariable() {
        Assertions.assertEquals("""
                final java.sql.PreparedStatement statement = connection.prepareStatement(query)""", generator.statementVariable().toString());
    }

    @Test
    void callableVariable() {
        Assertions.assertEquals("""
                final java.sql.CallableStatement statement = connection.prepareCall(query)""", generator.callableVariable().toString());
    }

    @Test
    void readMetaData() {
        Assertions.assertEquals("""
                final java.sql.ResultSetMetaData metaData = resultSet.getMetaData()""", generator.readMetaData().toString());
    }

    @Test
    void readColumnCount() {
        Assertions.assertEquals("""
                final int columnCount = metaData.getColumnCount()""", generator.readColumnCount().toString());
    }

    @Test
    void resultSetVariable() {
        Assertions.assertEquals("""
                final java.sql.ResultSet resultSet = statement.executeQuery()""", generator.resultSetVariable().toString());
    }

    @Test
    void executeUpdate() {
        Assertions.assertEquals("""
                return statement.executeUpdate();
                """, generator.executeUpdate().toString());
    }

    @Test
    void executeBatch() {
        Assertions.assertEquals("""
                return statement.executeBatch();
                """, generator.executeBatch().toString());
    }

    @Test
    void closeResultSet() {
        Assertions.assertEquals("""
                resultSet.close();
                """, generator.closeResultSet().toString());
    }

    @Test
    void closePrepareStatement() {
        Assertions.assertEquals("""
                statement.close();
                """, generator.closePrepareStatement().toString());
    }

    @Test
    void closeConnection() {
        Assertions.assertEquals("""
                connection.close();
                """, generator.closeConnection().toString());
    }

    @Test
    void closeState() {
        Assertions.assertEquals("""
                state.close();
                """, generator.closeState().toString());
    }

    @Test
    void executeStatement() {
        Assertions.assertEquals("""
                try (final java.sql.ResultSet resultSet = statement.executeQuery()) {
                """, generator.executeStatement().toString());
    }

    @Test
    void openConnection() {
        Assertions.assertEquals("""
                try (final java.sql.Connection connection = dataSource.getConnection()) {
                """, generator.openConnection().toString());
    }

    @Test
    void tryPrepareCallable() {
        Assertions.assertEquals("""
                try (final java.sql.CallableStatement statement = connection.prepareCall(query)) {
                """, generator.tryPrepareCallable().toString());
    }

    @Test
    void createStatement() {
        Assertions.assertEquals("""
                try (final java.sql.PreparedStatement statement = connection.prepareStatement(query)) {
                """, generator.createStatement().toString());
    }

    @Test
    void prepareBatch() {
        final var config = new SqlConfiguration();
        final var parameter = new SqlParameter();
        parameter.setName("test");
        config.getParameters().add(parameter);
        Assertions.assertEquals("""
                for (int batch = 0; batch < test.length; batch++) {
                  for (final int jdbcIndex : index.get("test")) {
                    statement.setObject(jdbcIndex, test[batch]);
                  }
                  statement.addBatch()}
                """, generator.prepareBatch(config).toString());
    }

}
