/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * Default implementation of the {@link Variables} interface. Uses {@link JavaConfiguration} to determine whether to
 * use keywords like 'final' or 'var'.
 */
public final class DefaultVariables implements Variables {

    private final JavaConfiguration javaConfiguration;

    public DefaultVariables(final JavaConfiguration javaConfiguration) {
        this.javaConfiguration = javaConfiguration;
    }

    @Override
    public CodeBlock inline(final Class<?> variableClass, final String name) {
        if (javaConfiguration.useFinal()) {
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
        final var code = leftHandSide("$N = $L");
        if (javaConfiguration.useVar()) {
            builder.add(code.toString(), name, initializer);
        } else {
            builder.add(code.toString(), variableType, name, initializer);
        }
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
        final var code = leftHandSide("$N = " + initializer);
        if (javaConfiguration.useVar()) {
            builder.add(code.toString(), Stream.concat(Stream.of(name), Arrays.stream(initializerArgs)).toArray());
        } else {
            builder.add(code.toString(), Stream.concat(Stream.of(variableType, name), Arrays.stream(initializerArgs)).toArray());
        }
        return builder.build();
    }

    private StringJoiner leftHandSide(final String closer) {
        final var code = new StringJoiner(" ");
        if (javaConfiguration.useFinal()) {
            code.add("final");
        }
        if (javaConfiguration.useVar()) {
            code.add("var");
        } else {
            code.add("$T");
        }
        code.add(closer);
        return code;
    }

}
