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

@DisplayName("DefaultRepositoryGenerator")
class DefaultRepositoryGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults extends RepositoryGeneratorTCK {

        @Override
        RepositoryGenerator generator() {
            return DaoObjectMother.defaultRepositoryGenerator(JavaConfigurations.defaults());
        }

        @Override
        String repositoryClassExpectation() {
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
                      private final com.example.persistence.converter.ToMapConverter toMap;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public Test(final javax.sql.DataSource dataSource) {
                        this.dataSource = dataSource;
                        this.toMap = new com.example.persistence.converter.ToMapConverter();
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
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(
                          final java.lang.Object test, final int id) {
                        LOG.entering("com.example.persistence.DataRepository", "queryData");
                        try {
                          final var query = QUERY_DATA;
                          LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                          final var rawQuery = QUERY_DATA_RAW;
                          final var index = QUERY_DATA_INDEX;
                          LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                          try (final var statement = connection.createStatement()) {
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
                            try (final var resultSet = statement.executeQuery(query)) {
                              final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                              while (resultSet.next()) {
                                list.add(toMap.apply(resultSet));
                              }
                              return list;
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

        @Override
        String repositoryInterfaceExpectation() {
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
                    public interface Test {
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(java.lang.Object test,
                          int id);
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using Java 11 configuration")
    class Java11 extends RepositoryGeneratorTCK {

        @Override
        RepositoryGenerator generator() {
            return DaoObjectMother.defaultRepositoryGenerator(JavaConfigurations.java11());
        }

        @Override
        String repositoryClassExpectation() {
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
                      private static final java.lang.String QUERY_DATA_RAW = "SELECT raw FROM table WHERE test = ? AND id = ?;";
                                        
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
                      private static final java.lang.String QUERY_DATA = "SELECT raw FROM table WHERE test = ? AND id = ?;";
                                        
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
                      private final com.example.persistence.converter.ToMapConverter toMap;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public Test(final javax.sql.DataSource dataSource) {
                        this.dataSource = dataSource;
                        this.toMap = new com.example.persistence.converter.ToMapConverter();
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
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(
                          final java.lang.Object test, final int id) {
                        LOG.entering("com.example.persistence.DataRepository", "queryData");
                        try {
                          final var query = QUERY_DATA;
                          LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                          final var rawQuery = QUERY_DATA_RAW;
                          final var index = QUERY_DATA_INDEX;
                          LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                          try (final var statement = connection.createStatement()) {
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
                            try (final var resultSet = statement.executeQuery(query)) {
                              final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                              while (resultSet.next()) {
                                list.add(toMap.apply(resultSet));
                              }
                              return list;
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

        @Override
        String repositoryInterfaceExpectation() {
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
                    public interface Test {
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(java.lang.Object test,
                          int id);
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using Java 14 configuration")
    class Java14 extends RepositoryGeneratorTCK {

        @Override
        RepositoryGenerator generator() {
            return DaoObjectMother.defaultRepositoryGenerator(JavaConfigurations.java14());
        }

        @Override
        String repositoryClassExpectation() {
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
                      private final com.example.persistence.converter.ToMapConverter toMap;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public Test(final javax.sql.DataSource dataSource) {
                        this.dataSource = dataSource;
                        this.toMap = new com.example.persistence.converter.ToMapConverter();
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
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(
                          final java.lang.Object test, final int id) {
                        LOG.entering("com.example.persistence.DataRepository", "queryData");
                        try {
                          final var query = QUERY_DATA;
                          LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                          final var rawQuery = QUERY_DATA_RAW;
                          final var index = QUERY_DATA_INDEX;
                          LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                          try (final var statement = connection.createStatement()) {
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
                            try (final var resultSet = statement.executeQuery(query)) {
                              final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                              while (resultSet.next()) {
                                list.add(toMap.apply(resultSet));
                              }
                              return list;
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

        @Override
        String repositoryInterfaceExpectation() {
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
                    public interface Test {
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(java.lang.Object test,
                          int id);
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using Java 16 configuration")
    class Java16 extends RepositoryGeneratorTCK {

        @Override
        RepositoryGenerator generator() {
            return DaoObjectMother.defaultRepositoryGenerator(JavaConfigurations.java16());
        }

        @Override
        String repositoryClassExpectation() {
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
                      private final com.example.persistence.converter.ToMapConverter toMap;
                                        
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public Test(final javax.sql.DataSource dataSource) {
                        this.dataSource = dataSource;
                        this.toMap = new com.example.persistence.converter.ToMapConverter();
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
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      public final java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(
                          final java.lang.Object test, final int id) {
                        LOG.entering("com.example.persistence.DataRepository", "queryData");
                        try {
                          final var query = QUERY_DATA;
                          LOG.finer(() -> java.lang.String.format("Picked query [%s]", "QUERY_DATA"));
                          final var rawQuery = QUERY_DATA_RAW;
                          final var index = QUERY_DATA_INDEX;
                          LOG.finer(() -> java.lang.String.format("Picked index [%s]", "QUERY_DATA_INDEX"));
                          try (final var statement = connection.createStatement()) {
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
                            try (final var resultSet = statement.executeQuery(query)) {
                              final var list = new java.util.ArrayList<java.util.Map<java.lang.String, java.lang.Object>>();
                              while (resultSet.next()) {
                                list.add(toMap.apply(resultSet));
                              }
                              return list;
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

        @Override
        String repositoryInterfaceExpectation() {
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
                    public interface Test {
                      /**
                       * <p>Executes the following statement:</p>
                       * <pre>
                       * SELECT raw FROM table WHERE test = ? AND id = ?;</pre>
                       *
                       * <p>Generated based on the following file(s):</p>
                       * <ul>
                       * <li>data/queryData.sql</li>
                       * </ul>
                       * <p>Disable generating this method by setting <strong>executeOnce</strong> to <strong>false</strong></p>
                       */
                      @javax.annotation.processing.Generated(
                          value = "YoSQL",
                          comments = "DO NOT MODIFY - automatically generated by YoSQL"
                      )
                      java.util.List<java.util.Map<java.lang.String, java.lang.Object>> queryData(java.lang.Object test,
                          int id);
                    }
                    """;
        }

    }

}
