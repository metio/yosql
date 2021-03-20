/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import wtf.metio.yosql.codegen.api.ConstructorGenerator;
import wtf.metio.yosql.codegen.tck.ConstructorTCK;
import wtf.metio.yosql.testing.configs.Java;

@DisplayName("JdbcConstructorGenerator")
final class JdbcConstructorGeneratorTest {

    @Nested
    @DisplayName("using default configuration")
    class Defaults implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.defaults());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 4 configuration")
    class Java4 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java4());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 5 configuration")
    class Java5 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java5());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 7 configuration")
    class Java7 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java7());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 8 configuration")
    class Java8 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java8());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 9 configuration")
    class Java9 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java9());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 11 configuration")
    class Java11 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java11());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 14 configuration")
    class Java14 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java14());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

    @Nested
    @DisplayName("using java 16 configuration")
    class Java16 implements ConstructorTCK {

        @Override
        public ConstructorGenerator generator() {
            return JdbcObjectMother.constructorGenerator(Java.java16());
        }

        @Override
        public String forRepositoryExpectation() {
            return """
                    @javax.annotation.processing.Generated(
                        value = "YoSQL",
                        comments = "DO NOT MODIFY - automatically generated by YoSQL"
                    )
                    public Constructor(final javax.sql.DataSource dataSource) {
                      this.dataSource = dataSource;
                      this.resultRow = new com.example.ToResultRowConverter();
                    }
                    """;
        }

    }

}