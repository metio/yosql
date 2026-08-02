/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;

@DisplayName("DefaultJdbcBlocks")
class DefaultJdbcBlocksTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults extends JdbcBlocksTCK {

        @Override
        JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.defaults());
        }

        @Override
        String getConnectionInlineExpectation() {
            return "final var connection = dataSource.getConnection()";
        }

        @Override
        String prepareStatementInlineExpectation() {
            return "final var statement = connection.prepareStatement(query)";
        }

        @Override
        String prepareCallInlineExpectation() {
            return "final var statement = connection.prepareCall(query)";
        }

        @Override
        String getMetaDataStatementExpectation() {
            return """
                    final var resultSetMetaData = resultSet.getMetaData();
                    """;
        }

        @Override
        String executeQueryInlineExpectation() {
            return "final var resultSet = statement.executeQuery()";
        }

        @Override
        String returnExecuteUpdateWithReturnExpectation() {
            return """
                    return statement.executeUpdate();
                    """;
        }

        @Override
        String returnExecuteUpdateWithoutReturnExpectation() {
            return """
                    statement.executeUpdate();
                    """;
        }

        @Override
        String executeBatchExpectation() {
            return """
                    return statement.executeBatch();
                    """;
        }

        @Override
        String closeResultSetExpectation() {
            return """
                    resultSet.close();
                    """;
        }

        @Override
        String closePrepareStatementExpectation() {
            return """
                    statement.close();
                    """;
        }

        @Override
        String closeConnectionExpectation() {
            return """
                    connection.close();
                    """;
        }

        @Override
        String closeConnectionWithGivenConnectionExpectation() {
            return """
                    """;
        }

        @Override
        String executeStatementExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery()) {
                    """;
        }

        @Override
        String executeStatementWithoutPreparationExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery(query)) {
                    """;
        }

        @Override
        String openConnectionExpectation() {
            return """
                    try (final var connection = dataSource.getConnection()) {
                    """;
        }

        @Override
        String openConnectionWithoutConnectionExpectation() {
            return """
                    try {
                    """;
        }

        @Override
        String openConnectionWithGivenConnectionWithoutRethrowExpectation() {
            return """
                    """;
        }

        @Override
        String tryPrepareCallableExpectation() {
            return """
                    try (final var statement = connection.prepareCall(query)) {
                    """;
        }

        @Override
        String createStatementExpectation() {
            return """
                    try (final var statement = connection.prepareStatement(query)) {
                    """;
        }

        @Override
        String createStatementWithoutPreparationExpectation() {
            return """
                    try (final var statement = connection.createStatement()) {
                    """;
        }

        @Override
        String prepareBatchExpectation() {
            return """
                    for (int batch = 0; batch < test.length; batch++) {
                      for (final int jdbcIndex : index.get("test")) {
                        statement.setObject(jdbcIndex, test[batch]);
                      }
                      for (final int jdbcIndex : index.get("id")) {
                        statement.setObject(jdbcIndex, id[batch]);
                      }
                      statement.addBatch();
                    }
                    """;
        }

        @Override
        String pickVendorQueryExpectation() {
            return """
                    final var query = QUERY_DATA;
                    LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                    final var rawQuery = QUERY_DATA_RAW;
                    final var index = QUERY_DATA_INDEX;
                    LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                    """;
        }

        @Override
        String logExecutedQueryExpectation() {
            return """
                    if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                      final var executedQuery = rawQuery
                        .replace(":test", test == null ? "null" : test.toString())
                        .replace(":id", java.lang.String.valueOf(id));
                      LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                    }
                    """;
        }

        @Override
        String logExecutedBatchQueryExpectation() {
            return """
                    if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                      final var executedQuery = rawQuery
                        .replace(":test", test == null ? "null" : java.util.Arrays.toString(test))
                        .replace(":id", java.util.Arrays.toString(id));
                      LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                    }
                    """;
        }

        @Override
        String returnAsMultipleExpectation() {
            return """
                    final var list = new java.util.ArrayList<com.example.Domain>();
                    while (resultSet.next()) {
                      list.add(converter.apply(resultSet));
                    }
                    return list;
                    """;
        }

        @Override
        String returnAsSingleExpectation() {
            return """
                    final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                    while (resultSet.next()) {
                      list.add(toMap.apply(resultSet));
                    }
                    return list.isEmpty() ? java.util.Optional.empty() : java.util.Optional.ofNullable(list.getFirst());
                    """;
        }

        @Override
        String returnAsSingleWithThrowExpectation() {
            return """
                    final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                    while (resultSet.next()) {
                      list.add(toMap.apply(resultSet));
                    }
                    if (list.size() > 1) {
                      throw new java.lang.IllegalStateException();
                    }
                    return list.isEmpty() ? java.util.Optional.empty() : java.util.Optional.ofNullable(list.getFirst());
                    """;
        }

        @Override
        String streamStatefulExpectation() {
            return """
                    return java.util.stream.StreamSupport.stream(new java.util.Spliterators.AbstractSpliterator<java.util.Map<java.lang.String, java.lang.Object>>(java.lang.Long.MAX_VALUE, java.util.Spliterator.ORDERED) {
                      @java.lang.Override
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final boolean tryAdvance(
                          final java.util.function.Consumer<? super java.util.Map<java.lang.String, java.lang.Object>> action) {
                        try {
                          if (resultSet.next()) {
                            action.accept(toMap.apply(resultSet));
                            return true;
                          }
                          return false;
                        }
                        catch (final java.sql.SQLException exception) {
                          throw new java.lang.RuntimeException(exception);
                        }
                      }
                    }, false).onClose(new java.lang.Runnable() {
                      @java.lang.Override
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final void run() {
                        try {
                          resultSet.close();
                          statement.close();
                          connection.close();
                        }
                        catch (final java.sql.SQLException exception) {
                          throw new java.lang.RuntimeException(exception);
                        }
                      }
                    });
                    """;
        }

        @Override
        String streamStatefulWithGivenConnectionExpectation() {
            return """
                    return java.util.stream.StreamSupport.stream(new java.util.Spliterators.AbstractSpliterator<java.util.Map<java.lang.String, java.lang.Object>>(java.lang.Long.MAX_VALUE, java.util.Spliterator.ORDERED) {
                      @java.lang.Override
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final boolean tryAdvance(
                          final java.util.function.Consumer<? super java.util.Map<java.lang.String, java.lang.Object>> action) {
                        try {
                          if (resultSet.next()) {
                            action.accept(toMap.apply(resultSet));
                            return true;
                          }
                          return false;
                        }
                        catch (final java.sql.SQLException exception) {
                          throw new java.lang.RuntimeException(exception);
                        }
                      }
                    }, false).onClose(new java.lang.Runnable() {
                      @java.lang.Override
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final void run() {
                        try {
                          resultSet.close();
                          statement.close();
                        }
                        catch (final java.sql.SQLException exception) {
                          throw new java.lang.RuntimeException(exception);
                        }
                      }
                    });
                    """;
        }

        @Override
        String setParametersExpectation() {
            return """
                    for (final int jdbcIndex : index.get("test")) {
                      statement.setObject(jdbcIndex, test);
                    }
                    for (final int jdbcIndex : index.get("id")) {
                      statement.setObject(jdbcIndex, id);
                    }
                    """;
        }

        @Override
        String setBatchParametersExpectation() {
            return """
                    for (final int jdbcIndex : index.get("test")) {
                      statement.setObject(jdbcIndex, test[batch]);
                    }
                    for (final int jdbcIndex : index.get("id")) {
                      statement.setObject(jdbcIndex, id[batch]);
                    }
                    """;
        }

    }

}
