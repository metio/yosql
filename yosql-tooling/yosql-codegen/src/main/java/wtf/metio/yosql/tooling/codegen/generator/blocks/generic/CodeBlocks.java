/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;

/**
 * Utility class for {@link CodeBlock} related code.
 */
public final class CodeBlocks {

    /**
     * Creates a new {@link CodeBlock} based on the provided String and arguments.
     *
     * @param format The code template to use.
     * @param args The template arguments to apply.
     * @return A new {@link CodeBlock} representing the resulting code.
     */
    public static CodeBlock code(final String format, final Object... args) {
        return CodeBlock.builder().add(format, args).build();
    }

    private CodeBlocks() {
        // factory class
    }

}
