/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import ch.qos.cal10n.MessageConveyor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.codegen.tck.RepositoryGeneratorTCK;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.testing.codegen.Blocks;

import java.util.Locale;

@DisplayName("JDBC GenericRepositoryGenerator")
class JdbcGenericRepositoryGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults implements RepositoryGeneratorTCK {

        @Override
        public GenericRepositoryGenerator generator() {
            return new GenericRepositoryGenerator(
                    new LocLoggerFactory(new MessageConveyor(Locale.ENGLISH)).getLocLogger("yosql.test"),
                    Blocks.annotationGenerator(),
                    Blocks.classes(),
                    Blocks.javadoc(),
                    JdbcObjectMother.fieldsGenerator(),
                    JdbcObjectMother.delegatingMethodsGenerator(),
                    PersistenceApis.JDBC);
        }

        @Override
        public String repositoryExpectation() {
            return """
                    /**
                     * Generated based on the following file(s):
                     * <ul>
                     * <li>data/queryData.sql</li>
                     * </ul>
                     */
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public final class Test {
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(com.example.persistence.DataRepository.class.getName());
                                        
                      /**
                       * Generated based on the following file:
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private static final java.lang.String QUERY_DATA_RAW = ""\"
                      SELECT raw FROM table WHERE test = ? AND id = ?;""\";
                                        
                      /**
                       * Generated based on the following file:
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private static final java.lang.String QUERY_DATA = ""\"
                      SELECT raw FROM table WHERE test = ? AND id = ?;""\";
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private static final java.util.Map<java.lang.String, int[]> QUERY_DATA_INDEX = new java.util.HashMap<>(2);
                                        
                      static {
                        QUERY_DATA_INDEX.put("test", new int[] { 0 });
                        QUERY_DATA_INDEX.put("id", new int[] { 1 });
                      }
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private final javax.sql.DataSource dataSource;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      private final com.example.persistence.util.ToResultRowConverter resultRow;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public Test(final javax.sql.DataSource dataSource) {
                        this.dataSource = dataSource;
                        this.resultRow = new com.example.ToResultRowConverter();
                      }
                                        
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>generateBlockingApi</strong> to <strong>false</strong></p>
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
                              final io.reactivex.Emitter<com.example.util.ResultRow> emitter) throws
                              java.lang.Exception {
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
                                        
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>generateMutinyApi</strong> to <strong>false</strong></p>
                       *
                       * @see com.example.util.ResultRow
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final io.smallrye.mutiny.Multi<com.example.util.ResultRow> queryDataMulti(
                          final java.lang.Object test, final int id) {
                        LOG.entering("com.example.persistence.DataRepository", "queryDataMulti");
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
                              return io.smallrye.mutiny.Multi.createFrom().iterable(list);
                            }
                          }
                        }
                        catch (final java.sql.SQLException exception) {
                          throw new java.lang.RuntimeException(exception);
                        }
                      }
                    }
                    """;
        }

    }

}