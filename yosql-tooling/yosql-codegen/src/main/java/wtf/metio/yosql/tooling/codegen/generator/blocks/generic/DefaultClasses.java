/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.tooling.codegen.model.configuration.JavaConfiguration;
import wtf.metio.yosql.std.generator.StandardClasses;

import javax.lang.model.element.Modifier;

final class DefaultClasses implements Classes {

    private final JavaConfiguration java;

    DefaultClasses(final JavaConfiguration java) {
        this.java = java;
    }

    @Override
    public TypeSpec.Builder publicClass(final ClassName name) {
        final var builder = StandardClasses.openClass(name);
        if (java.useFinal()) {
            builder.addModifiers(Modifier.FINAL);
        }
        return builder;
    }

    @Override
    public TypeSpec.Builder openClass(final ClassName name) {
        return StandardClasses.openClass(name);
    }

}
