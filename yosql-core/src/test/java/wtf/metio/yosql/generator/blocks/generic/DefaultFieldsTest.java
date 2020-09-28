/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.annotationGenerator;

@DisplayName("DefaultFields")
class DefaultFieldsTest {

    @Test
    @DisplayName("creates fields")
    void shouldCreateField() {
        // given
        final var fields = new DefaultFields(annotationGenerator());

        // when
        final var field = fields.field(String.class, "test");

        // then
        Assertions.assertEquals("""
                @javax.annotation.processing.Generated(
                    value = "test",
                    comments = "field"
                )
                private final java.lang.String test;
                """, field.toString());
    }

    @Test
    @DisplayName("creates fields for TypeNames")
    void shouldCreateFieldWithTypeName() {
        // given
        final var fields = new DefaultFields(annotationGenerator());

        // when
        final var field = fields.field(TypeName.BOOLEAN, "test");

        // then
        Assertions.assertEquals("""
                @javax.annotation.processing.Generated(
                    value = "test",
                    comments = "field"
                )
                private final boolean test;
                """, field.toString());
    }

}