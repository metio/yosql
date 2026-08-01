/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import javax.lang.model.element.Modifier;
import java.util.List;

public final class DefaultMethods implements Methods {

    private final Annotations annotations;
    private final Javadoc javadoc;
    private final JavaConfiguration java;

    public DefaultMethods(final Annotations annotations, final Javadoc javadoc, final JavaConfiguration java) {
        this.annotations = annotations;
        this.javadoc = javadoc;
        this.java = java;
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name) {
        final var modifiers = java.useFinalMethods()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotations(annotations.generatedMethod());
    }

    @Override
    public MethodSpec.Builder publicMethod(
            final String name,
            final List<SqlStatement> statements,
            final String configuration) {
        final var modifiers = java.useFinalMethods()
                ? List.of(Modifier.PUBLIC, Modifier.FINAL)
                : List.of(Modifier.PUBLIC);
        return MethodSpec.methodBuilder(name)
                .addModifiers(modifiers)
                .addAnnotations(annotations.generatedMethod())
                .addAnnotations(annotations.generatedMethod(SqlConfiguration.fromStatements(statements)))
                .addJavadoc(javadoc.methodJavadoc(statements, configuration));
    }

    @Override
    public MethodSpec.Builder declaration(
            final String name,
            final List<SqlStatement> statements,
            final String configuration) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotations(annotations.generatedMethod())
                .addAnnotations(annotations.generatedMethod(SqlConfiguration.fromStatements(statements)))
                .addJavadoc(javadoc.methodJavadoc(statements, configuration));
    }

    @Override
    public MethodSpec.Builder implementation(final String name) {
        final var modifiers = java.useFinalMethods()
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
                .addJavadoc(javadoc.constructorJavadoc())
                .addAnnotations(annotations.generatedMethod())
                .addAnnotations(annotations.generatedConstructor());
    }

}
