/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
    }

}
