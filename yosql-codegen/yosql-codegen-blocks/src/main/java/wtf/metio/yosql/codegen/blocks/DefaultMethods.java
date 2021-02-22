/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.AnnotationGenerator;
import wtf.metio.yosql.codegen.api.Javadoc;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.lang.model.element.Modifier;
import java.util.List;

public final class DefaultMethods implements Methods {

    private final AnnotationGenerator annotations;
    private final Javadoc javadoc;
    private final JavaConfiguration java;

    public DefaultMethods(final AnnotationGenerator annotations, final Javadoc javadoc, final JavaConfiguration java) {
        this.annotations = annotations;
        this.javadoc = javadoc;
        this.java = java;
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name) {
        final var modifiers = java.useFinal()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotations(annotations.generatedMethod());
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name, final List<SqlStatement> statements) {
        final var modifiers = java.useFinal()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotations(annotations.generatedMethod())
                .addJavadoc(javadoc.methodJavadoc(statements));
    }

    @Override
    public MethodSpec.Builder implementation(final String name) {
        final var modifiers = java.useFinal()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotation(Override.class)
                .addAnnotations(annotations.generatedMethod());
    }

    @Override
    public MethodSpec.Builder constructor() {
        return MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotations(annotations.generatedMethod());
    }

}
