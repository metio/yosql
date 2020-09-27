package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;

@DisplayName("DefaultParameters")
class DefaultParametersTest {

    @Test
    @DisplayName("generates parameter")
    void shouldGenerateParameter() {
        // given
        final var generator = new DefaultParameters(names());

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
        final var generator = new DefaultParameters(names());

        // when
        final var parameter = generator.parameter(TypeName.BOOLEAN, "test");

        // then
        Assertions.assertEquals("""
                final boolean test""", parameter.toString());
    }

}