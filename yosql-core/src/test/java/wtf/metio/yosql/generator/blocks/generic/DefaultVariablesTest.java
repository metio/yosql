/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.configuration.VariableConfiguration;

import static wtf.metio.yosql.model.options.VariableTypeOptions.VAR;

@DisplayName("DefaultVariables")
class DefaultVariablesTest {

    @Test
    @DisplayName("creates variables")
    void shouldCreateVariable() {
        // given
        final var variables = new DefaultVariables(VariableConfiguration.usingDefaults());

        // when
        final var variable = variables.variable("test", String.class);

        // then
        Assertions.assertEquals("""
                final java.lang.String test""", variable.toString());
    }

    @Test
    @DisplayName("creates variables with the 'var' keyword")
    void shouldCreateVariableWithVarKeyword() {
        // given
        final var config = VariableConfiguration.usingDefaults()
                .withVariableType(VAR);
        final var variables = new DefaultVariables(config);

        // when
        final var variable = variables.variable("test", String.class);

        // then
        Assertions.assertEquals("""
                final var test""", variable.toString());
    }

}
