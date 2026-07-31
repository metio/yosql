/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

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
        Assertions.assertEquals(code, codeBlock.toString());
    }

}
