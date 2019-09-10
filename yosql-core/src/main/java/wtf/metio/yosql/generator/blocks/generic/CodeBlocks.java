package wtf.metio.yosql.generator.blocks.generic;

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
