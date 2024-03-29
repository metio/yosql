/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
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
