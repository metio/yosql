package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

@DisplayName("DefaultParameters")
class DefaultParametersTest extends ValidationFileTest {

    @Test
    @DisplayName("generates parameter")
    void shouldGenerateParameter(final ValidationFile validationFile) {
        // given
        final var generator = new DefaultParameters(names());

        // when
        final var parameter = generator.parameter(String.class, "test");

        // then
        validate(parameter, validationFile);
    }

    @Test
    @DisplayName("generates parameter with TypeName")
    void shouldGenerateParameterWithTypeName(final ValidationFile validationFile) {
        // given
        final var generator = new DefaultParameters(names());

        // when
        final var parameter = generator.parameter(TypeName.BOOLEAN, "test");

        // then
        validate(parameter, validationFile);
    }

}