package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.names;
import static wtf.metio.yosql.generator.blocks.generic.GenericBlocksObjectMother.variables;

@DisplayName("DefaultControlFlows")
class DefaultControlFlowsTest extends ValidationFileTest {

    private DefaultControlFlows generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultControlFlows(variables(), names());
    }

    @Test
    @DisplayName("try with resource")
    void shouldTryResource(final ValidationFile validationFile) {
        validate(generator.tryWithResource(CodeBlocks.code("resource")), validationFile);
    }

    @Test
    @DisplayName("if has next")
    void shouldProceedForNext(final ValidationFile validationFile) {
        validate(generator.ifHasNext(), validationFile);
    }

}