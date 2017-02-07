package com.github.sebhoss.yosql.generator.helpers;

import com.squareup.javapoet.MethodSpec;

public class TypicalMethods {

    public static MethodSpec.Builder publicMethod(final String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(TypicalModifiers.PUBLIC_METHOD);
    }

    public static MethodSpec.Builder implementation(final String name) {
        return publicMethod(name).addAnnotation(Override.class);
    }

    public static MethodSpec.Builder constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(TypicalModifiers.PUBLIC_CONSTRUCTOR);
    }

}
