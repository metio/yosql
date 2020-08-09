package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

@DisplayName("DefaultFields")
class DefaultFieldsTest extends ValidationFileTest {

    @Test
    @DisplayName("creates fields")
    void shouldCreateField(final ValidationFile validationField) {
        // given
        final var fields = new DefaultFields(GenericBlocksObjectMother.annotationGenerator());

        // when
        final var field = fields.field(String.class, "test");

        // then
        validate(field, validationField);
    }

    @Test
    @DisplayName("creates fields for TypeNames")
    void shouldCreateFieldWithTypeName(final ValidationFile validationField) {
        // given
        final var fields = new DefaultFields(GenericBlocksObjectMother.annotationGenerator());

        // when
        final var field = fields.field(TypeName.BOOLEAN, "test");

        // then
        validate(field, validationField);
    }

}