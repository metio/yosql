/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.*;
import wtf.metio.yosql.internals.javapoet.TypicalTypes;

import javax.lang.model.element.Modifier;

abstract class AbstractConfigurationGroup {

    protected static final String MAVEN_PACKAGE = "wtf.metio.yosql.tooling.maven";
    protected static final String GRADLE_PACKAGE = "wtf.metio.yosql.tooling.gradle";

    protected static ClassName mavenType(final String className) {
        return ClassName.get(MAVEN_PACKAGE, className);
    }

    protected static ClassName gradleType(final String className) {
        return ClassName.get(GRADLE_PACKAGE, className);
    }

    protected static FieldSpec mavenStringParameter(final String name) {
        return mavenStringParameter(name, "");
    }

    protected static FieldSpec mavenStringParameter(final String name, final String defaultValue) {
        return FieldSpec.builder(ClassName.get(String.class), name)
                .addAnnotation(mavenParameter(defaultValue))
                .initializer("$S", defaultValue)
                .build();
    }

    protected static FieldSpec mavenParameter(final TypeName type, final String name) {
        return FieldSpec.builder(type, name)
                .addAnnotation(mavenParameter(""))
                .build();
    }

    private static AnnotationSpec mavenParameter(final String defaultValue) {
        return AnnotationSpec.builder(TypicalTypes.MAVEN_PARAMETER)
                .addMember("required", "true")
                .addMember("defaultValue", "$S", defaultValue)
                .build();
    }

    protected static MethodSpec gradleConstructor() {
        return MethodSpec.constructorBuilder()
                .addJavadoc("Required by Gradle")
                .addAnnotation(TypicalTypes.INJECT)
                .addModifiers(Modifier.PUBLIC)
                .build();
    }

    protected static MethodSpec gradleStringProperty(final String name, final String description) {
        return MethodSpec.methodBuilder(name)
                .addJavadoc("@return " + description + ".")
                .addAnnotation(TypicalTypes.GRADLE_INPUT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypicalTypes.GRADLE_STRING_PROPERTY)
                .build();
    }

    protected static MethodSpec gradleListProperty(final TypeName type, final String name, final String description) {
        return MethodSpec.methodBuilder(name)
                .addJavadoc("@return " + description + ".")
                .addAnnotation(TypicalTypes.GRADLE_INPUT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypicalTypes.gradleListPropertyOf(type))
                .build();
    }

    protected static MethodSpec gradleNamedProperty(final TypeName type, final String name, final String description) {
        return MethodSpec.methodBuilder(name)
                .addJavadoc("@return " + description + ".")
                .addAnnotation(TypicalTypes.GRADLE_INPUT)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(TypicalTypes.gradleContainerOf(type))
                .build();
    }

}
