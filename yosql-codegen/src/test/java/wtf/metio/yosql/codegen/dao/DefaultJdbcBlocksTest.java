/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
        String executeStatementExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery()) {
                    """;
        }

        @Override
        String openConnectionExpectation() {
            return """
                    try (final var connection = dataSource.getConnection()) {
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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

    @Nested
    @DisplayName("using Java 11 configuration")
    class Java11 extends JdbcBlocksTCK {

        @Override
        JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java11());
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
        String executeStatementExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery()) {
                    """;
        }

        @Override
        String openConnectionExpectation() {
            return """
                    try (final var connection = dataSource.getConnection()) {
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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

    @Nested
    @DisplayName("using Java 14 configuration")
    class Java14 extends JdbcBlocksTCK {

        @Override
        JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java14());
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
        String executeStatementExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery()) {
                    """;
        }

        @Override
        String openConnectionExpectation() {
            return """
                    try (final var connection = dataSource.getConnection()) {
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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

    @Nested
    @DisplayName("using Java 16 configuration")
    class Java16 extends JdbcBlocksTCK {

        @Override
        JdbcBlocks generator() {
            return DaoObjectMother.jdbcBlocks(JavaConfigurations.java16());
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
        String executeStatementExpectation() {
            return """
                    try (final var resultSet = statement.executeQuery()) {
                    """;
        }

        @Override
        String openConnectionExpectation() {
            return """
                    try (final var connection = dataSource.getConnection()) {
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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
                    return list.size() > 0 ? java.util.Optional.of(list.get(0)) : java.util.Optional.empty();
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
