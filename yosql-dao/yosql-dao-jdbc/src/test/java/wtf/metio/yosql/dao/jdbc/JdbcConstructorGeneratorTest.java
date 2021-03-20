/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.*;
import wtf.metio.yosql.codegen.api.ConstructorGenerator;
import wtf.metio.yosql.testing.configs.Java;
import wtf.metio.yosql.testing.configs.Sql;

@DisplayName("JdbcConstructorGenerator")
final class JdbcConstructorGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.defaults());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 4 configuration")
    class Java4 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java4());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 5 configuration")
    class Java5 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java5());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 7 configuration")
    class Java7 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java7());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 8 configuration")
    class Java8 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java8());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 9 configuration")
    class Java9 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java9());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 11 configuration")
    class Java11 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java11());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 14 configuration")
    class Java14 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java14());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

    @Nested
    @DisplayName("using java 16 configuration")
    class Java16 {

        private ConstructorGenerator generator;

        @BeforeEach
        void setup() {
            generator = JdbcObjectMother.constructorGenerator(Java.java16());
        }

        @Test
        void shouldGenerateConstructor() {
            Assertions.assertEquals("""
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """, generator.forRepository(Sql.sqlStatements()).toString());
        }

    }

}
