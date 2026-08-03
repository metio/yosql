/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DefaultParameters")
class DefaultParametersTest {

    @Test
    @DisplayName("generates parameter")
    void shouldGenerateParameter() {
        // given
        final var generator = new DefaultParameters();

        // when
        final var parameter = generator.parameter(String.class, "test");

        // then
        Assertions.assertEquals("""
                final java.lang.String test""", parameter.toString());
    }

    @Test
    @DisplayName("generates parameter with TypeName")
    void shouldGenerateParameterWithTypeName() {
        // given
        final var generator = new DefaultParameters();

        // when
        final var parameter = generator.parameter(TypeName.BOOLEAN, "test");

        // then
        Assertions.assertEquals("""
                final boolean test""", parameter.toString());
    }

}
