/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.model.configuration.JavaConfiguration;

import javax.lang.model.element.Modifier;
import java.lang.reflect.Type;

final class DefaultFields implements Fields {

    private final AnnotationGenerator annotations;
    private final JavaConfiguration java;

    DefaultFields(final AnnotationGenerator annotations, final JavaConfiguration java) {
        this.annotations = annotations;
        this.java = java;
    }

    @Override
    public FieldSpec field(final Type type, final String name) {
        return field(TypeName.get(type), name);
    }

    @Override
    public FieldSpec field(final TypeName type, final String name) {
        final var builder = builder(type, name).addModifiers(Modifier.PRIVATE);
        if (java.useFinal()) {
            builder.addModifiers(Modifier.FINAL);
        }
        return builder.build();
    }

    @Override
    public FieldSpec.Builder prepareConstant(final Type type, final String name) {
        return prepareConstant(TypeName.get(type), name);
    }

    @Override
    public FieldSpec.Builder prepareConstant(final TypeName type, final String name) {
        return builder(type, name).addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);
    }

    private FieldSpec.Builder builder(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotations(annotations.generatedField());
    }

}
