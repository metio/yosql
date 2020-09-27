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