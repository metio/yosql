/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.generator.helpers.TypicalTypes;
import wtf.metio.yosql.model.sql.SqlObjectMother;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.*;
import static wtf.metio.yosql.generator.blocks.jdbc.JdbcObjectMother.*;
import static wtf.metio.yosql.generator.logging.LoggingObjectMother.loggingGenerator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.runtimeConfiguration;
import static wtf.metio.yosql.model.sql.SqlObjectMother.sqlConfiguration;

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
                        final java.sql.Connection connection = dataSource.getConnection()""",
                generator.connectionVariable().toString());
    }

    @Test
    void statementVariable() {
        Assertions.assertEquals("""
                        final java.sql.PreparedStatement statement = connection.prepareStatement(query)""",
                generator.statementVariable().toString());
    }

    @Test
    void callableVariable() {
        Assertions.assertEquals("""
                        final java.sql.CallableStatement statement = connection.prepareCall(query)""",
                generator.callableVariable().toString());
    }

    @Test
    void readMetaData() {
        Assertions.assertEquals("""
                        final java.sql.ResultSetMetaData metaData = resultSet.getMetaData()""",
                generator.readMetaData().toString());
    }

    @Test
    void readColumnCount() {
        Assertions.assertEquals("""
                        final int columnCount = metaData.getColumnCount()""",
                generator.readColumnCount().toString());
    }

    @Test
    void resultSetVariable() {
        Assertions.assertEquals("""
                        final java.sql.ResultSet resultSet = statement.executeQuery()""",
                generator.resultSetVariable().toString());
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
        Assertions.assertEquals("""
                for (int batch = 0; batch < test.length; batch++) {
                  for (final int jdbcIndex : index.get("test")) {
                    statement.setObject(jdbcIndex, test[batch]);
                  }
                  statement.addBatch()}
                """, generator.prepareBatch(sqlConfiguration()).toString());
    }

    @Test
    void pickVendorQuery() {
        Assertions.assertEquals("""
                final java.lang.String query = QUERY_TEST;
                LOG.finer(() -> String.format("Picked query [%s]", "QUERY_TEST"));
                final java.lang.String rawQuery = QUERY_TEST_RAW;
                final java.util.Map<java.lang.String, int[]> index = QUERY_TEST_INDEX;
                LOG.finer(() -> String.format("Picked index [%s]", "QUERY_TEST_INDEX"));
                """, generator.pickVendorQuery(SqlObjectMother.sqlStatements()).toString());
    }

    @Test
    void logExecutedQuery() {
        Assertions.assertEquals("""
                if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                  final java.lang.String executedQuery = rawQuery
                    .replace(":test", test == null ? "null" : test.toString());
                  LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                }
                """, generator.logExecutedQuery(sqlConfiguration()).toString());
    }

    @Test
    void logExecutedBatchQuery() {
        Assertions.assertEquals("""
                if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                  final java.lang.String executedQuery = rawQuery
                    .replace(":test", test == null ? "null" : java.util.Arrays.toString(test));
                  LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                }
                """, generator.logExecutedBatchQuery(sqlConfiguration()).toString());
    }

    @Test
    void prepareReturnList() {
        Assertions.assertEquals("""
                final java.lang.Object list = new java.util.ArrayList<>();
                while (state.next()) {
                  list.add(converter.asUserType(state));
                }
                """, generator.prepareReturnList(TypeName.OBJECT, "converter").build().toString());
    }

    @Test
    void returnAsList() {
        Assertions.assertEquals("""
                final java.util.List<java.lang.Object> list = new java.util.ArrayList<>();
                while (state.next()) {
                  list.add(converter.asUserType(state));
                }
                return list;
                """, generator.returnAsList(TypicalTypes.listOf(TypeName.OBJECT), "converter").toString());
    }

    @Test
    void returnAsStream() {
        Assertions.assertEquals("""
                final java.util.List<java.lang.Object> list = new java.util.ArrayList<>();
                while (state.next()) {
                  list.add(converter.asUserType(state));
                }
                return list.stream();
                """, generator.returnAsStream(TypicalTypes.listOf(TypeName.OBJECT), "converter").toString());
    }

    @Test
    void createResultState() {
        Assertions.assertEquals("""
                        final com.example.state.ResultState state = new com.example.state.ResultState(resultSet, metaData, columnCount)""",
                generator.createResultState().toString());
    }

    @Test
    void returnNewFlowState() {
        Assertions.assertEquals("""
                return new com.example.state.FlowState(connection, statement, resultSet, metaData, columnCount);
                """, generator.returnNewFlowState().toString());
    }

    @Test
    void setParameters() {
        Assertions.assertEquals("""
                for (final int jdbcIndex : index.get("test")) {
                  statement.setObject(jdbcIndex, test);
                }
                """, generator.setParameters(sqlConfiguration()).toString());
    }

    @Test
    void setBatchParameters() {
        Assertions.assertEquals("""
                for (final int jdbcIndex : index.get("test")) {
                  statement.setObject(jdbcIndex, test[batch]);
                }
                """, generator.setBatchParameters(sqlConfiguration()).toString());
    }

}
