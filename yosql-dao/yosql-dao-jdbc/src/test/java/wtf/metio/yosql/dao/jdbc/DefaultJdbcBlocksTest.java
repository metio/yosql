/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;
import wtf.metio.yosql.testing.configs.Sql;

@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    private JdbcBlocks generator;

    @BeforeEach
    void setUp() {
        generator = JdbcObjectMother.jdbcBlocks();
    }

    @Test
    void connectionVariable() {
        Assertions.assertEquals("""
                        final var connection = dataSource.getConnection()""",
                generator.connectionVariable().toString());
    }

    @Test
    void statementVariable() {
        Assertions.assertEquals("""
                        final var statement = connection.prepareStatement(query)""",
                generator.statementVariable().toString());
    }

    @Test
    void callableVariable() {
        Assertions.assertEquals("""
                        final var statement = connection.prepareCall(query)""",
                generator.callableVariable().toString());
    }

    @Test
    void readMetaData() {
        Assertions.assertEquals("""
                        final var metaData = resultSet.getMetaData();
                        """,
                generator.readMetaData().toString());
    }

    @Test
    void readColumnCount() {
        Assertions.assertEquals("""
                        final var columnCount = metaData.getColumnCount();
                        """,
                generator.readColumnCount().toString());
    }

    @Test
    void resultSetVariable() {
        Assertions.assertEquals("""
                        final var resultSet = statement.executeQuery()""",
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
                try (final var resultSet = statement.executeQuery()) {
                """, generator.executeStatement().toString());
    }

    @Test
    void openConnection() {
        Assertions.assertEquals("""
                try (final var connection = dataSource.getConnection()) {
                """, generator.openConnection().toString());
    }

    @Test
    void tryPrepareCallable() {
        Assertions.assertEquals("""
                try (final var statement = connection.prepareCall(query)) {
                """, generator.tryPrepareCallable().toString());
    }

    @Test
    void createStatement() {
        Assertions.assertEquals("""
                try (final var statement = connection.prepareStatement(query)) {
                """, generator.createStatement().toString());
    }

    @Test
    void prepareBatch() {
        Assertions.assertEquals("""
                for (int batch = 0; batch < test.length; batch++) {
                  for (final int jdbcIndex : index.get("test")) {
                    statement.setObject(jdbcIndex, test[batch]);
                  }
                  for (final int jdbcIndex : index.get("id")) {
                    statement.setObject(jdbcIndex, id[batch]);
                  }
                  statement.addBatch()}
                """, generator.prepareBatch(Sql.sqlConfiguration()).toString());
    }

    @Test
    void pickVendorQuery() {
        Assertions.assertEquals("""
                final java.lang.String query = QUERY_DATA;
                LOG.finer(() -> String.format("Picked query [%s]", "QUERY_DATA"));
                final java.lang.String rawQuery = QUERY_DATA_RAW;
                final java.util.Map<java.lang.String, int[]> index = QUERY_DATA_INDEX;
                LOG.finer(() -> String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                """, generator.pickVendorQuery(Sql.sqlStatements()).toString());
    }

    @Test
    void logExecutedQuery() {
        Assertions.assertEquals("""
                if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                  final java.lang.String executedQuery = rawQuery
                    .replace(":test", test == null ? "null" : test.toString())
                    .replace(":id", java.lang.String.valueOf(id));
                  LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                }
                """, generator.logExecutedQuery(Sql.sqlConfiguration()).toString());
    }

    @Test
    void logExecutedBatchQuery() {
        Assertions.assertEquals("""
                if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                  final java.lang.String executedQuery = rawQuery
                    .replace(":test", test == null ? "null" : java.util.Arrays.toString(test))
                    .replace(":id", java.util.Arrays.toString(id));
                  LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                }
                """, generator.logExecutedBatchQuery(Sql.sqlConfiguration()).toString());
    }

    @Test
    void returnAsList() {
        Assertions.assertEquals("""
                final var list = new java.util.ArrayList<java.lang.Object>();
                while (state.next()) {
                  list.add(converter.asUserType(state));
                }
                return list;
                """, generator.returnAsList(TypicalTypes.listOf(TypeName.OBJECT), "converter").toString());
    }

    @Test
    void returnAsStream() {
        Assertions.assertEquals("""
                final var list = new java.util.ArrayList<java.lang.Object>();
                while (state.next()) {
                  list.add(converter.asUserType(state));
                }
                return list.stream();
                """, generator.returnAsStream(TypicalTypes.listOf(TypeName.OBJECT), "converter").toString());
    }

    @Test
    void createResultState() {
        Assertions.assertEquals("""
                        final var state = new com.example.persistence.util.ResultState(resultSet, metaData, columnCount);
                        """,
                generator.createResultState().toString());
    }

    @Test
    void returnNewFlowState() {
        Assertions.assertEquals("""
                return new com.example.persistence.util.FlowState(connection, statement, resultSet, metaData, columnCount);
                """, generator.returnNewFlowState().toString());
    }

    @Test
    void setParameters() {
        Assertions.assertEquals("""
                for (final int jdbcIndex : index.get("test")) {
                  statement.setObject(jdbcIndex, test);
                }
                for (final int jdbcIndex : index.get("id")) {
                  statement.setObject(jdbcIndex, id);
                }
                """, generator.setParameters(Sql.sqlConfiguration()).toString());
    }

    @Test
    void setBatchParameters() {
        Assertions.assertEquals("""
                for (final int jdbcIndex : index.get("test")) {
                  statement.setObject(jdbcIndex, test[batch]);
                }
                for (final int jdbcIndex : index.get("id")) {
                  statement.setObject(jdbcIndex, id[batch]);
                }
                """, generator.setBatchParameters(Sql.sqlConfiguration()).toString());
    }

}