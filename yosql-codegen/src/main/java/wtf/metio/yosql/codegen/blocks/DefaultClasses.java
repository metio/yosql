/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

import javax.lang.model.element.Modifier;

public final class DefaultClasses implements Classes {

    private final JavaConfiguration java;

    public DefaultClasses(final JavaConfiguration java) {
        this.java = java;
    }

    @Override
    public TypeSpec.Builder publicInterface(final ClassName name) {
        return TypeSpec.interfaceBuilder(name)
                .addModifiers(Modifier.PUBLIC);
    }

    @Override
    public TypeSpec.Builder publicClass(final ClassName name) {
        final var builder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
        if (java.useFinalClasses()) {
            builder.addModifiers(Modifier.FINAL);
        }
        return builder;
    }

    @Override
    public TypeSpec.Builder openClass(final ClassName name) {
        return TypeSpec.classBuilder(name).addModifiers(Modifier.PUBLIC);
    }

}
