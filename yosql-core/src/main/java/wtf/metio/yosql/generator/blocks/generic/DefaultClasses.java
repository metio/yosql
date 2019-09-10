package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.generator.blocks.api.Classes;

import javax.lang.model.element.Modifier;

final class DefaultClasses implements Classes {

    @Override
    public TypeSpec.Builder publicClass(final ClassName name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public TypeSpec.Builder openClass(final ClassName name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

}
