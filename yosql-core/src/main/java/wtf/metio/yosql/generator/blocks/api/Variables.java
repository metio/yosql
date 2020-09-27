package wtf.metio.yosql.generator.blocks.api;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;

public interface Variables {

    CodeBlock variable(String name, Class<?> variableClass);

    CodeBlock variable(String name, TypeName variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, CodeBlock initializer);

    CodeBlock variableStatement(String name, Class<?> variableClass, CodeBlock initializer);

    CodeBlock variable(String name, Class<?> variableClass, String initializer, Object... initializerArgs);

}
