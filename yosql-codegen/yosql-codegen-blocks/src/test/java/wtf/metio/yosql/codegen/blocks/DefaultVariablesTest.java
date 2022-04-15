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
import wtf.metio.yosql.testing.configs.Java;

@DisplayName("DefaultVariables")
class DefaultVariablesTest {

    @Nested
    @DisplayName("Java Defaults")
    class JavaDefaultsTest {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.defaults());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.defaults());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.defaults());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 4 Preset")
    class Java4Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java4());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java4());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java4());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 5 Preset")
    class Java5Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java5());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java5());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java5());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 8 Preset")
    class Java8Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java8());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java8());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java8());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 9 Preset")
    class Java9Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java9());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java9());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java9());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final java.lang.String test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 11 Preset")
    class Java11Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java11());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java11());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java11());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 14 Preset")
    class Java14Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java14());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java14());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java14());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

    @Nested
    @DisplayName("Java 16 Preset")
    class Java16Test {

        @Test
        @DisplayName("creates variables")
        void shouldCreateVariable() {
            // given
            final var variables = new DefaultVariables(Java.java16());

            // when
            final var variable = variables.variable("test", String.class);

            // then
            Assertions.assertEquals("""
                    final java.lang.String test""", variable.toString());
        }

        @Test
        @DisplayName("creates variables with initializer")
        void shouldCreateVariableWithInitializer() {
            // given
            final var variables = new DefaultVariables(Java.java16());

            // when
            final var variable = variables.variable("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world" """, variable.toString());
        }

        @Test
        @DisplayName("creates variable statements")
        void shouldCreateVariableStatement() {
            // given
            final var variables = new DefaultVariables(Java.java16());

            // when
            final var variable = variables.variableStatement("test", String.class, CodeBlocks.code("$S", "hello world"));

            // then
            Assertions.assertEquals("""
                    final var test = "hello world";
                    """, variable.toString());
        }

    }

}
