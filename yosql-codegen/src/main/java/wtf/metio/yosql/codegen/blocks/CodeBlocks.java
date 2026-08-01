/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import wtf.metio.yosql.models.configuration.ResultRowConverter;

/**
 * Utility class for {@link CodeBlock} related code.
 */
public interface CodeBlocks {

    /**
     * Creates a new {@link CodeBlock} based on the provided String and arguments.
     *
     * @param format The code template to use.
     * @param args   The template arguments to apply.
     * @return A new {@link CodeBlock} representing the resulting code.
     */
    static CodeBlock code(final String format, final Object... args) {
        return CodeBlock.builder().add(format, args).build();
    }

    CodeBlock returnTrue();

    CodeBlock returnFalse();

    CodeBlock close(String resource);

    CodeBlock initializeFieldToSelf(String fieldName);

    CodeBlock returnValue(CodeBlock value);

    CodeBlock initializeConverter(ResultRowConverter converter);

}
