package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.variableConfiguration;
import static wtf.metio.yosql.model.options.VariableTypeOptions.VAR;

@DisplayName("DefaultVariables")
class DefaultVariablesTest extends ValidationFileTest {

    @Test
    @DisplayName("creates variables")
    void shouldCreateVariable(final ValidationFile validationFile) {
        // given
        final var config = variableConfiguration();
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
        final var config = variableConfiguration(VAR);
        final var variables = new DefaultVariables(config);

        // when
        final var code = variables.variable("test", String.class);

        // then
        validate(code, validationFile);
    }

}
