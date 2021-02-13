/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.model.configuration.JavaConfiguration;

import java.util.StringJoiner;

final class DefaultVariables implements Variables {

    private final JavaConfiguration javaConfiguration;

    DefaultVariables(final JavaConfiguration javaConfiguration) {
        this.javaConfiguration = javaConfiguration;
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass) {
        if (javaConfiguration.useFinal()) {
            if (javaConfiguration.useVar()) {
                return CodeBlock.builder().add("final var $N", name).build();
            }
            return CodeBlock.builder().add("final $T $N", variableClass, name).build();
        }
        if (javaConfiguration.useVar()) {
            return CodeBlock.builder().add("var $N", name).build();
        }
        return CodeBlock.builder().add("$T $N", variableClass, name).build();
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass, final CodeBlock initializer) {
        return variable(name, TypeName.get(variableClass), initializer);
    }

    @Override
    public CodeBlock variable(final String name, final TypeName variableClass, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = $L");
        if (javaConfiguration.useVar()) {
            builder.add(code.toString(), name, initializer);
        } else {
            builder.add(code.toString(), variableClass, name, initializer);
        }
        return builder.build();
    }

    @Override
    public CodeBlock variableStatement(final String name, final Class<?> variableClass, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = $L");
        if (javaConfiguration.useVar()) {
            builder.addStatement(code.toString(), name, initializer);
        } else {
            builder.addStatement(code.toString(), variableClass, name, initializer);
        }
        return builder.build();
    }

    @Override
    public CodeBlock variable(
            final String name,
            final Class<?> variableClass,
            final String initializer,
            final Object... initializerArgs) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = " + initializer);
        if (javaConfiguration.useVar()) {
            builder.add(code.toString(), name, initializerArgs);
        } else {
            builder.add(code.toString(), variableClass, name, initializerArgs);
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
