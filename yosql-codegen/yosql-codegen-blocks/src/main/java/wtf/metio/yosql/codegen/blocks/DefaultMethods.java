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

    public MethodSpec.Builder standardMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateStandardApi");
    }

    public MethodSpec.Builder batchMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateBatchApi");
    }

    public MethodSpec.Builder streamEagerMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateStreamEagerApi");
    }

    public MethodSpec.Builder streamLazyMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateStreamLazyApi");
    }

    public MethodSpec.Builder rxJavaMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateRxJavaApi");
    }

    public MethodSpec.Builder reactorMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateReactorApi");
    }

    public MethodSpec.Builder mutinyMethod(final String name, final List<SqlStatement> statements) {
        return publicMethod(name, statements, "generateMutinyApi");
    }

    public MethodSpec.Builder publicMethod(
            final String name, 
            final List<SqlStatement> statements, 
            final String configuration) {
        final var modifiers = java.useFinal()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotations(annotations.generatedMethod())
                .addJavadoc(javadoc.methodJavadoc(statements, configuration));
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
