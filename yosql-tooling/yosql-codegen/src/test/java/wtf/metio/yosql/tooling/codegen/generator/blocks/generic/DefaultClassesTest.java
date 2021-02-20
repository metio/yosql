/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.tooling.codegen.model.configuration.JavaConfiguration;

@DisplayName("DefaultClasses")
class DefaultClassesTest {

    private DefaultClasses generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultClasses(JavaConfiguration.usingDefaults());
    }

    @Test
    @DisplayName("generates open classes")
    void shouldGenerateOpenClass() {
        Assertions.assertEquals("""
                public class TestClass {
                }
                """, generator.openClass(ClassName.bestGuess("TestClass")).build().toString());
    }

    @Test
    @DisplayName("generates public classes")
    void shouldGeneratePublicClass() {
        Assertions.assertEquals("""
                public final class TestClass {
                }
                """, generator.publicClass(ClassName.bestGuess("TestClass")).build().toString());
    }

}