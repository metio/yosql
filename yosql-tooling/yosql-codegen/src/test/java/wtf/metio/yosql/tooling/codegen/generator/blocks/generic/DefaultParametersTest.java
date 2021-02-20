/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.tooling.codegen.test.ObjectMother;

@DisplayName("DefaultParameters")
class DefaultParametersTest {

    @Test
    @DisplayName("generates parameter")
    void shouldGenerateParameter() {
        // given
        final var generator = new DefaultParameters(ObjectMother.names(), RuntimeConfiguration.usingDefaults());

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
        final var generator = new DefaultParameters(ObjectMother.names(), RuntimeConfiguration.usingDefaults());

        // when
        final var parameter = generator.parameter(TypeName.BOOLEAN, "test");

        // then
        Assertions.assertEquals("""
                final boolean test""", parameter.toString());
    }

}