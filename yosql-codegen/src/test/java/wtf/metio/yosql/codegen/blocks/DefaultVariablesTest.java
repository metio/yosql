/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;

@DisplayName("DefaultVariables")
class DefaultVariablesTest {

    @Nested
    @DisplayName("using default configuration")
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
    @DisplayName("using Java 11 configuration")
    class Java11Test {

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
    @DisplayName("using Java 14 configuration")
    class Java14Test {

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
    @DisplayName("using Java 16 configuration")
    class Java16Test {

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

}
