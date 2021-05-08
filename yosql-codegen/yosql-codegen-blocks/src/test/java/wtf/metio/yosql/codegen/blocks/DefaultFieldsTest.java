/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testing.configs.Annotations;
import wtf.metio.yosql.testing.configs.Apis;
import wtf.metio.yosql.testing.configs.Java;

@DisplayName("DefaultFields")
class DefaultFieldsTest {

    private DefaultFields generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultFields(new DefaultAnnotationGenerator(Annotations.defaults(), Apis.defaults()), Java.defaults());
    }

    @Test
    @DisplayName("creates fields")
    void shouldCreateField() {
        Assertions.assertEquals("""
                @javax.annotation.processing.Generated(
                    value = "YoSQL",
                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                )
                private final java.lang.String test;
                """, generator.field(String.class, "test").toString());
    }

    @Test
    @DisplayName("creates fields for TypeNames")
    void shouldCreateFieldWithTypeName() {
        Assertions.assertEquals("""
                @javax.annotation.processing.Generated(
                    value = "YoSQL",
                    comments = "DO NOT MODIFY - automatically generated by YoSQL"
                )
                private final boolean test;
                """, generator.field(TypeName.BOOLEAN, "test").toString());
    }

}
