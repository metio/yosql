package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.generator.blocks.api.GenericBlocks;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

@DisplayName("DefaultGenericBlocks")
class DefaultGenericBlocksTest extends ValidationFileTest {

    private GenericBlocks blocks;

    @BeforeEach
    void setUp() {
        blocks = new DefaultGenericBlocks();
    }

    @Test
    @DisplayName("returns true")
    void shouldReturnTrue(final ValidationFile validationFile) {
        validate(blocks.returnTrue(), validationFile);
    }

    @Test
    @DisplayName("returns false")
    void shouldReturnFalse(final ValidationFile validationFile) {
        validate(blocks.returnFalse(), validationFile);
    }

}