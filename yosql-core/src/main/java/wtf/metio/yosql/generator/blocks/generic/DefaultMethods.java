/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.Javadoc;
import wtf.metio.yosql.generator.blocks.api.Methods;
import wtf.metio.yosql.model.sql.SqlStatement;

import javax.lang.model.element.Modifier;
import java.util.List;

final class DefaultMethods implements Methods {

    private final AnnotationGenerator annotations;
    private final Javadoc javadoc;

    DefaultMethods(final AnnotationGenerator annotations, final Javadoc javadoc) {
        this.annotations = annotations;
        this.javadoc = javadoc;
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotations(annotations.generatedMethod());
    }

    @Override
    public MethodSpec.Builder publicMethod(final String name, final List<SqlStatement> statements) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addAnnotations(annotations.generatedMethod())
                .addJavadoc(javadoc.methodJavadoc(statements));
    }

    @Override
    public MethodSpec.Builder implementation(final String name) {
        return MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
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
