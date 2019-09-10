package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.CodeBlock;

public interface Variables {

    CodeBlock variable(String name, Class<?> variableClass);

    CodeBlock variable(String name, Class<?> variableClass, CodeBlock initializer);
    CodeBlock variableStatement(String name, Class<?> variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, String initializer, Object... initializerArgs);

}
