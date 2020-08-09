package wtf.metio.yosql.generator.blocks.generic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("CodeBlocks")
class CodeBlocksTest {

    @Test
    void shouldCreateCodeBlock() {
        // given
        final var code = "1 + 2";

        // when
        final var codeBlock = CodeBlocks.code(code);

        // then
        Assertions.assertNotNull(codeBlock);
    }

}