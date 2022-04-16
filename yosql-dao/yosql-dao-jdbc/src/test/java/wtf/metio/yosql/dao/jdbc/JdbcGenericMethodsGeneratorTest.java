/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.*;
import wtf.metio.yosql.codegen.blocks.GenericMethodsGenerator;
import wtf.metio.yosql.testing.configs.Sql;

@DisplayName("JDBC GenericMethodsGenerator")
class JdbcGenericMethodsGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults {

        private GenericMethodsGenerator generator;

        @BeforeEach
        void setUp() {
            generator = JdbcObjectMother.genericMethodsGenerator();
        }

        @Test
        void constructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.constructor(Sql.sqlStatements()).toString());
        }

        @Test
        void standardReadMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateStandardApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final java.util.List<com.example.util.ResultRow> queryData(final java.lang.Object test,
                        final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryData");
                      try (final var connection = dataSource.getConnection()) {
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        try (final var statement = connection.prepareStatement(query)) {
                          for (final int jdbcIndex : index.get("test")) {
                            statement.setObject(jdbcIndex, test);
                          }
                          for (final int jdbcIndex : index.get("id")) {
                            statement.setObject(jdbcIndex, id);
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : test.toString())
                              .replace(":id", java.lang.String.valueOf(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          try (final var resultSet = statement.executeQuery()) {
                            final var resultSetMetaData = resultSet.getMetaData();
                            final var columnCount = resultSetMetaData.getColumnCount();
                            final var state = new com.example.persistence.util.ResultState(resultSet, resultSetMetaData, columnCount);
                            final var list = new java.util.ArrayList<com.example.util.ResultRow>();
                            while (state.next()) {
                              list.add(resultRow.apply(state));
                            }
                            return list;
                          }
                        }
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.standardReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void standardWriteMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateStandardApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final int queryData(final java.lang.Object test, final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryData");
                      try (final var connection = dataSource.getConnection()) {
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        try (final var statement = connection.prepareStatement(query)) {
                          for (final int jdbcIndex : index.get("test")) {
                            statement.setObject(jdbcIndex, test);
                          }
                          for (final int jdbcIndex : index.get("id")) {
                            statement.setObject(jdbcIndex, id);
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : test.toString())
                              .replace(":id", java.lang.String.valueOf(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          return statement.executeUpdate();
                        }
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.standardWriteMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void standardCallMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateStandardApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final java.util.List<com.example.util.ResultRow> queryData(final java.lang.Object test,
                        final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryData");
                      try (final var connection = dataSource.getConnection()) {
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        try (final var statement = connection.prepareCall(query)) {
                          for (final int jdbcIndex : index.get("test")) {
                            statement.setObject(jdbcIndex, test);
                          }
                          for (final int jdbcIndex : index.get("id")) {
                            statement.setObject(jdbcIndex, id);
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : test.toString())
                              .replace(":id", java.lang.String.valueOf(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          try (final var resultSet = statement.executeQuery()) {
                            final var resultSetMetaData = resultSet.getMetaData();
                            final var columnCount = resultSetMetaData.getColumnCount();
                            final var state = new com.example.persistence.util.ResultState(resultSet, resultSetMetaData, columnCount);
                            final var list = new java.util.ArrayList<com.example.util.ResultRow>();
                            while (state.next()) {
                              list.add(resultRow.apply(state));
                            }
                            return list;
                          }
                        }
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.standardCallMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void batchWriteMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateBatchApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final int[] queryDataBatch(final java.lang.Object[] test, final int[] id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryDataBatch");
                      try (final var connection = dataSource.getConnection()) {
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        try (final var statement = connection.prepareStatement(query)) {
                          for (int batch = 0; batch < test.length; batch++) {
                            for (final int jdbcIndex : index.get("test")) {
                              statement.setObject(jdbcIndex, test[batch]);
                            }
                            for (final int jdbcIndex : index.get("id")) {
                              statement.setObject(jdbcIndex, id[batch]);
                            }
                            statement.addBatch();
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : java.util.Arrays.toString(test))
                              .replace(":id", java.util.Arrays.toString(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          return statement.executeBatch();
                        }
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.batchWriteMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void streamEagerReadMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateStreamEagerApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final java.util.stream.Stream<com.example.util.ResultRow> queryDataStreamEager(
                        final java.lang.Object test, final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryDataStreamEager");
                      try (final var connection = dataSource.getConnection()) {
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        try (final var statement = connection.prepareStatement(query)) {
                          for (final int jdbcIndex : index.get("test")) {
                            statement.setObject(jdbcIndex, test);
                          }
                          for (final int jdbcIndex : index.get("id")) {
                            statement.setObject(jdbcIndex, id);
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : test.toString())
                              .replace(":id", java.lang.String.valueOf(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          try (final var resultSet = statement.executeQuery()) {
                            final var resultSetMetaData = resultSet.getMetaData();
                            final var columnCount = resultSetMetaData.getColumnCount();
                            final var state = new com.example.persistence.util.ResultState(resultSet, resultSetMetaData, columnCount);
                            final var list = new java.util.ArrayList<com.example.util.ResultRow>();
                            while (state.next()) {
                              list.add(resultRow.apply(state));
                            }
                            return list.stream();
                          }
                        }
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.streamEagerReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void streamLazyReadMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateStreamLazyApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final java.util.stream.Stream<com.example.util.ResultRow> queryDataStreamLazy(
                        final java.lang.Object test, final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryDataStreamLazy");
                      try {
                        final var connection = dataSource.getConnection();
                        final var query = QUERY_DATA;
                        LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                        final var rawQuery = QUERY_DATA_RAW;
                        final var index = QUERY_DATA_INDEX;
                        LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                        final var statement = connection.prepareStatement(query);
                        for (final int jdbcIndex : index.get("test")) {
                          statement.setObject(jdbcIndex, test);
                        }
                        for (final int jdbcIndex : index.get("id")) {
                          statement.setObject(jdbcIndex, id);
                        }
                        if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                          final var executedQuery = rawQuery
                            .replace(":test", test == null ? "null" : test.toString())
                            .replace(":id", java.lang.String.valueOf(id));
                          LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                        }
                        final var resultSet = statement.executeQuery();
                        final var resultSetMetaData = resultSet.getMetaData();
                        final var columnCount = resultSetMetaData.getColumnCount();
                        final var state = new com.example.persistence.util.ResultState(resultSet, resultSetMetaData, columnCount);
                        return java.util.stream.StreamSupport.stream(new java.util.Spliterators.AbstractSpliterator<com.example.util.ResultRow>(java.lang.Long.MAX_VALUE, java.util.Spliterator.ORDERED) {
                          @java.lang.Override
                          @javax.annotation.processing.Generated(
                              value = "YoSQL",
                              comments = "DO NOT MODIFY - automatically generated by YoSQL"
                          )
                          public final boolean tryAdvance(
                              final java.util.function.Consumer<? super com.example.util.ResultRow> action) {
                            try {
                              if (state.next()) {
                                action.accept(resultRow.apply(state));
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
                      }
                      catch (final java.sql.SQLException exception) {
                        throw new java.lang.RuntimeException(exception);
                      }
                    }
                    """, generator.streamLazyReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }

        @Test
        void rxJavaReadMethod() {
            Assertions.assertEquals("""
                    /**
                     * <p>Executes the following statement:</p>
                     * <pre>
                     * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                     *
                     * <p>Generated based on the following file(s):</p>
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     * <p>Disable generating this method by setting <strong>generateRxJavaApi</strong> to <strong>false</strong></p>
                     *
                     * @see com.example.util.ResultRow
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final io.reactivex.Flowable<com.example.util.ResultRow> queryDataFlow(
                        final java.lang.Object test, final int id) {
                      LOG.entering("com.example.persistence.DataRepository", "queryDataFlow");
                      return io.reactivex.Flowable.generate(new java.util.concurrent.Callable<com.example.persistence.util.FlowState>() {
                        @java.lang.Override
                        @javax.annotation.processing.Generated(
                            value = "YoSQL",
                            comments = "DO NOT MODIFY - automatically generated by YoSQL"
                        )
                        public final com.example.persistence.util.FlowState call() throws java.lang.Exception {
                          final var connection = dataSource.getConnection();
                          final var query = QUERY_DATA;
                          LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                          final var rawQuery = QUERY_DATA_RAW;
                          final var index = QUERY_DATA_INDEX;
                          LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                          final var statement = connection.prepareStatement(query);
                          for (final int jdbcIndex : index.get("test")) {
                            statement.setObject(jdbcIndex, test);
                          }
                          for (final int jdbcIndex : index.get("id")) {
                            statement.setObject(jdbcIndex, id);
                          }
                          if (LOG.isLoggable(java.util.logging.Level.FINE)) {
                            final var executedQuery = rawQuery
                              .replace(":test", test == null ? "null" : test.toString())
                              .replace(":id", java.lang.String.valueOf(id));
                            LOG.fine(() -> java.lang.String.format("Executing query [%s]", executedQuery));
                          }
                          final var resultSet = statement.executeQuery();
                          final var resultSetMetaData = resultSet.getMetaData();
                          final var columnCount = resultSetMetaData.getColumnCount();
                          return new com.example.persistence.util.FlowState(connection, statement, resultSet, resultSetMetaData, columnCount);
                        }
                      }, new io.reactivex.functions.BiConsumer<com.example.persistence.util.FlowState, io.reactivex.Emitter<com.example.util.ResultRow>>() {
                        @java.lang.Override
                        @javax.annotation.processing.Generated(
                            value = "YoSQL",
                            comments = "DO NOT MODIFY - automatically generated by YoSQL"
                        )
                        public final void accept(final com.example.persistence.util.FlowState state,
                            final io.reactivex.Emitter<com.example.util.ResultRow> emitter) throws java.lang.Exception {
                          try {
                            if (state.next()) {
                              emitter.onNext(resultRow.apply(state));
                            } else {
                              emitter.onComplete();
                            }
                          }
                          catch (final java.sql.SQLException exception) {
                            emitter.onError(exception);
                          }
                        }
                      }, new io.reactivex.functions.Consumer<com.example.persistence.util.FlowState>() {
                        @java.lang.Override
                        @javax.annotation.processing.Generated(
                            value = "YoSQL",
                            comments = "DO NOT MODIFY - automatically generated by YoSQL"
                        )
                        public final void accept(final com.example.persistence.util.FlowState state) throws
                            java.lang.Exception {
                          state.close();
                        }
                      });
                    }
                    """, generator.rxJavaReadMethod(Sql.sqlConfiguration(), Sql.sqlStatements()).toString());
        }
    }

}