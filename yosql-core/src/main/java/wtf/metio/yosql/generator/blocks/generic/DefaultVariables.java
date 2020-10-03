/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.generator.blocks.api.Variables;
import wtf.metio.yosql.model.configuration.VariableConfiguration;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;
import java.util.StringJoiner;

import static java.util.stream.Collectors.joining;

final class DefaultVariables implements Variables {

    private final VariableConfiguration options;

    DefaultVariables(final VariableConfiguration options) {
        this.options = options;
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass) {
        final var modifiers = options.modifiers();
        if (modifiers.isEmpty()) {
            if (VariableTypeOptions.VAR.equals(options.variableType())) {
                return CodeBlock.builder().add("var $N", name).build();
            }
            return CodeBlock.builder().add("$T $N", variableClass, name).build();
        }
        final var modifier = modifiers.stream().map(Modifier::toString).collect(joining(" "));
        if (VariableTypeOptions.VAR.equals(options.variableType())) {
            return CodeBlock.builder().add("$N var $N", modifier, name).build();
        }
        return CodeBlock.builder().add("$N $T $N", modifier, variableClass, name).build();
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass, final CodeBlock initializer) {
        return variable(name, TypeName.get(variableClass), initializer);
    }

    @Override
    public CodeBlock variable(final String name, final TypeName variableClass, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = $L");
        if (options.variableType() == VariableTypeOptions.VAR) {
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
        if (options.variableType() == VariableTypeOptions.VAR) {
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
        if (options.variableType() == VariableTypeOptions.VAR) {
            builder.add(code.toString(), name, initializerArgs);
        } else {
            builder.add(code.toString(), variableClass, name, initializerArgs);
        }
        return builder.build();
    }

    private StringJoiner leftHandSide(final String closer) {
        final var code = new StringJoiner(" ");
        final var modifiers = options.modifiers();
        if (!modifiers.isEmpty()) {
            modifiers.forEach(modifier -> code.add(modifier.toString()));
        }
        if (options.variableType() == VariableTypeOptions.VAR) {
            code.add("var");
        } else {
            code.add("$T");
        }
        code.add(closer);
        return code;
    }

}
