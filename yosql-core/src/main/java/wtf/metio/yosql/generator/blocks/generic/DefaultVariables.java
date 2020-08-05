package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.generator.blocks.api.Variables;
import wtf.metio.yosql.model.configuration.VariableConfiguration;

import java.util.StringJoiner;

final class DefaultVariables implements Variables {

    private final VariableConfiguration options;

    DefaultVariables(final VariableConfiguration options) {
        this.options = options;
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass) {
        final var modifiers = options.modifiers();
        if (modifiers.isEmpty()) {
            return CodeBlock.builder().add("$T $N", name, variableClass).build();
        }
        return CodeBlock.builder().add("$N $T $N", modifiers, name, variableClass).build();
    }

    @Override
    public CodeBlock variable(final String name, final ClassName variableClass, CodeBlock initializer) {
        return variable(name, variableClass.toString(), initializer);
    }

    @Override
    public CodeBlock variable(final String name, final Class<?> variableClass, final CodeBlock initializer) {
        return variable(name, variableClass.toString(), initializer);
    }
    
    private CodeBlock variable(final String name, final String variableClass, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = $L");
        switch (options.variableType()) {
            case VAR -> builder.add(code.toString(), name, initializer);
            default -> builder.add(code.toString(), variableClass, name, initializer);
        }
        return builder.build();
    }

    @Override
    public CodeBlock variableStatement(final String name, final Class<?> variableClass, final CodeBlock initializer) {
        final var builder = CodeBlock.builder();
        final var code = leftHandSide("$N = $L");
        switch (options.variableType()) {
            case VAR -> builder.addStatement(code.toString(), name, initializer);
            default -> builder.addStatement(code.toString(), variableClass, name, initializer);
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
        switch (options.variableType()) {
            case VAR -> builder.add(code.toString(), name, initializerArgs);
            default -> builder.add(code.toString(), variableClass, name, initializerArgs);
        }
        return builder.build();
    }

    private StringJoiner leftHandSide(final String closer) {
        final var code = new StringJoiner(" ");
        final var modifiers = options.modifiers();
        if (!modifiers.isEmpty()) {
            modifiers.forEach(modifier -> code.add(modifier.toString()));
        }
        switch (options.variableType()) {
            case VAR -> code.add("var");
            default -> code.add("$T");
        }
        code.add(closer);
        return code;
    }

}
