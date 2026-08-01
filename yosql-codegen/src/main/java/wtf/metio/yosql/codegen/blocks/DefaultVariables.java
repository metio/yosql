/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Default implementation of the {@link Variables} interface. Uses {@link JavaConfiguration} to determine whether to
 * use keywords like 'final' or 'var'.
 */
public final class DefaultVariables implements Variables {

    private final JavaConfiguration java;

    public DefaultVariables(final JavaConfiguration java) {
        this.java = java;
    }

    @Override
    public CodeBlock inline(final Class<?> variableClass, final String name) {
        if (java.useFinalVariables()) {
            return CodeBlock.builder().add("final $T $N", variableClass, name).build();
        }
        return CodeBlock.builder().add("$T $N", variableClass, name).build();
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
        if (java.useFinalVariables()) {
            code.add("final");
        }
        code.add("var");
        code.add(closer);
        return code;
    }

}
