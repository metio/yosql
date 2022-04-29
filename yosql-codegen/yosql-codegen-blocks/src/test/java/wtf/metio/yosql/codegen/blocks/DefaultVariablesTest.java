/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.configs.JavaConfigurations;

@DisplayName("DefaultVariables")
class DefaultVariablesTest {

    @Nested
    @DisplayName("JavaConfigurations Defaults")
    class JavaDefaultsTest {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.defaults());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.defaults());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.defaults());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 4 Preset")
    class Java4Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java4());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java4());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java4());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 5 Preset")
    class Java5Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java5());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java5());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java5());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 8 Preset")
    class Java8Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java8());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java8());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java8());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 9 Preset")
    class Java9Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java9());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java9());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java9());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 11 Preset")
    class Java11Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java11());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java11());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java11());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 14 Preset")
    class Java14Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java14());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java14());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java14());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("JavaConfigurations 16 Preset")
    class Java16Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java16());

            // when
            final var variable = variables.inline(String.class, "test");

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java16());

            // when
            final var variable = variables.inline(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(JavaConfigurations.java16());

            // when
            final var variable = variables.statement(String.class, "test", CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

}
