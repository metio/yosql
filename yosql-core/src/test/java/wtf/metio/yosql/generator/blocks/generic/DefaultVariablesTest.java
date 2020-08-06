package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.model.configuration.VariableConfiguration;
import wtf.metio.yosql.model.options.VariableTypeOptions;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import javax.lang.model.element.Modifier;
import java.util.List;

@DisplayName("DefaultVariables")
class DefaultVariablesTest extends ValidationFileTest {

    @Test
    @DisplayName("creates variables")
    void shouldCreateVariable(final ValidationFile validationFile) {
        // given
        final var config = VariableConfiguration.builder()
                .setModifiers(List.of(Modifier.FINAL))
                .setVariableType(VariableTypeOptions.TYPE)
                .build();
        final var variables = new DefaultVariables(config);

        // when
        final var code = variables.variable("test", String.class);

        // then
        validate(code, validationFile);
    }

    @Test
    @DisplayName("creates variables with the 'var' keyword")
    void shouldCreateVariableWithVarKeyword(final ValidationFile validationFile) {
        // given
        final var config = VariableConfiguration.builder()
                .setModifiers(List.of())
                .setVariableType(VariableTypeOptions.VAR)
                .build();
        final var variables = new DefaultVariables(config);

        // when
        final var code = variables.variable("test", String.class);

        // then
        validate(code, validationFile);
    }

}
