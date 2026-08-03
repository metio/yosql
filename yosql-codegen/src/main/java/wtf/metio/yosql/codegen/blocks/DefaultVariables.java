/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Default implementation of the {@link Variables} interface. Declares every local {@code final var}:
 * a generated method reassigns nothing, and saying so is what lets a reader stop tracking it.
 */
public final class DefaultVariables implements Variables {

    public DefaultVariables() {
    }

    @Override
    public CodeBlock inline(final Class<?> variableClass, final String name) {
        return CodeBlock.builder().add("final $T $N", variableClass, name).build();
    }

    @Override
    public CodeBlock inline(final Class<?> variableClass, final String name, final CodeBlock initializer) {
        return inline(TypeName.get(variableClass), name, initializer);
    }

    @Override
    public CodeBlock inline(final TypeName variableType, final String name, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        builder.add(leftHandSide("$N = $L").toString(), name, initializer);
        return builder.build();
    }

    @Override
    public CodeBlock statement(final Class<?> variableClass, final String name, final CodeBlock initializer) {
        return statement(TypeName.get(variableClass), name, initializer);
    }

    @Override
    public CodeBlock statement(final TypeName variableType, final String name, final CodeBlock initializer) {
        return CodeBlock.builder().addStatement(inline(variableType, name, initializer)).build();
    }

    @Override
    public CodeBlock inline(
            final Class<?> variableClass,
            final String name,
            final String initializer,
            final Object... initializerArgs) {
        return inline(TypeName.get(variableClass), name, initializer, initializerArgs);
    }

    @Override
    public CodeBlock inline(
            final TypeName variableType,
            final String name,
            final String initializer,
            final Object... initializerArgs) {
        final var builder = CodeBlock.builder();
        builder.add(leftHandSide("$N = " + initializer).toString(),
                Stream.concat(Stream.of(name), Arrays.stream(initializerArgs)).toArray());
        return builder.build();
    }

    private StringJoiner leftHandSide(final String closer) {
        final var code = new StringJoiner(" ");
        code.add("final");
        code.add("var");
        code.add(closer);
        return code;
    }

}
