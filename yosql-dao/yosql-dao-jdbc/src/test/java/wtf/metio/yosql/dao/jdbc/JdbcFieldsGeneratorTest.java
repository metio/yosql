/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.*;
import wtf.metio.yosql.codegen.api.FieldsGenerator;
import wtf.metio.yosql.codegen.tck.FieldsGeneratorTCK;
import wtf.metio.yosql.internals.junit5.TestIterables;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.Java;
import wtf.metio.yosql.testing.configs.Jdbc;
import wtf.metio.yosql.testing.configs.Sql;
import wtf.metio.yosql.testing.logging.Loggers;

@DisplayName("JdbcFieldsGenerator")
final class JdbcFieldsGeneratorTest {

    @Nested
    @Disabled
    @DisplayName("using default configuration")
    class Defaults implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 4 configuration")
    class Java4 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java4()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 5 configuration")
    class Java5 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java5()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 7 configuration")
    class Java7 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java7()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 8 configuration")
    class Java8 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java8()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 9 configuration")
    class Java9 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java9()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 11 configuration")
    class Java11 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java11()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 14 configuration")
    class Java14 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java14()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

    @Nested
    @Disabled
    @DisplayName("using java 16 configuration")
    class Java16 implements FieldsGeneratorTCK {

        @Override
        public FieldsGenerator generator() {
            return new JdbcFieldsGenerator(
                    Jdbc.defaults(), Loggers.loggingGenerator(), Blocks.javadoc(), Blocks.fields(Java.java16()),
                    new DefaultJdbcFields(Jdbc.defaults()));
        }

        @Override
        public String staticInitializerExpectation() {
            return """
                QUERY_TEST_INDEX.put("test", new int[] { 0 });
                """;
        }

        @Override
        public String[] asFieldsExpectations() {
            return new String[]{
                    """
                            private final javax.sql.DataSource dataSource;
                            """,
                    """
                            private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Test.class.getName());
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST_RAW = "SELECT raw FROM table;";
                            """,
                    """
                            /**
                             * Generated based on the following file:
                             * <ul>
                             * <li>/some/path/query.sql</li>
                             * </ul>
                             */
                            private static final java.lang.String QUERY_TEST = "SELECT raw FROM table;";
                            """,
                    """
                            private static final java.util.Map<java.lang.String, int[]> QUERY_TEST_INDEX = new java.util.HashMap<>(1);
                            """
            };
        }

    }

}