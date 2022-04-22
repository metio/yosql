/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.internals.javapoet;

import com.squareup.javapoet.*;

import java.lang.reflect.Type;

public final class TypicalTypes {

    public static final ClassName OBJECT = ClassName.get("java.lang", "Object");
    public static final ClassName STRING = ClassName.get("java.lang", "String");
    public static final ClassName BOOLEAN = ClassName.get("java.lang", "Boolean");

    public static final ClassName LIST = ClassName.get("java.util", "List");
    public static final ClassName MAP = ClassName.get("java.util", "Map");
    public static final ClassName SET = ClassName.get("java.util", "Set");
    public static final ClassName OPTIONAL = ClassName.get("java.util", "Optional");
    public static final ClassName STREAM = ClassName.get("java.util.stream", "Stream");
    public static final ClassName CONSUMER = ClassName.get("java.util.function", "Consumer");

    public static final ClassName PATH = ClassName.get("java.nio.file", "Path");
    public static final ClassName CHARSET = ClassName.get("java.nio.charset", "Charset");
    public static final ClassName INTEGER = ClassName.get("java.lang", "Integer");

    public static final ClassName FLOWABLE = ClassName.get("io.reactivex", "Flowable");
    public static final ClassName MULTI = ClassName.get("io.smallrye.mutiny", "Multi");
    public static final ClassName FLUX = ClassName.get("reactor.core.publisher", "Flux");

    public static final ClassName GRADLE_PROPERTY = ClassName.bestGuess("org.gradle.api.provider.Property");
    public static final ClassName GRADLE_LIST_PROPERTY = ClassName.bestGuess("org.gradle.api.provider.ListProperty");
    public static final ClassName GRADLE_INPUT = ClassName.bestGuess("org.gradle.api.tasks.Input");
    public static final ClassName GRADLE_INPUT_DIRECTORY = ClassName.bestGuess("org.gradle.api.tasks.InputDirectory");
    public static final ClassName GRADLE_OUTPUT_DIRECTORY = ClassName.bestGuess("org.gradle.api.tasks.OutputDirectory");
    public static final ClassName GRADLE_CONTAINERS = ClassName.bestGuess("org.gradle.api.NamedDomainObjectContainer");
    public static final ClassName GRADLE_DIRECTORY = ClassName.bestGuess("org.gradle.api.file.DirectoryProperty");
    public static final ClassName GRADLE_LAYOUT = ClassName.bestGuess("org.gradle.api.file.ProjectLayout");
    public static final ClassName GRADLE_OBJECTS = ClassName.bestGuess("org.gradle.api.model.ObjectFactory");

    public static final TypeName ARRAY_OF_INTS = ArrayTypeName.of(int.class);

    public static final TypeName MAP_OF_STRING_AND_ARRAY_OF_INTS = mapOf(STRING, ARRAY_OF_INTS);
    public static final TypeName MAP_OF_STRING_AND_OBJECTS = mapOf(STRING, OBJECT);

    public static ParameterizedTypeName mapOf(final TypeName key, final TypeName value) {
        return ParameterizedTypeName.get(MAP, key, value);
    }

    public static ParameterizedTypeName optionalOf(final Type type) {
        return optionalOf(TypeName.get(type));
    }

    public static ParameterizedTypeName optionalOf(final TypeName type) {
        return ParameterizedTypeName.get(OPTIONAL, type);
    }

    public static ParameterizedTypeName listOf(final Type type) {
        return listOf(TypeName.get(type));
    }

    public static ParameterizedTypeName listOf(final TypeName type) {
        return ParameterizedTypeName.get(LIST, type);
    }

    public static ParameterizedTypeName setOf(final TypeName type) {
        return ParameterizedTypeName.get(SET, type);
    }

    public static ParameterizedTypeName streamOf(final TypeName type) {
        return ParameterizedTypeName.get(STREAM, type);
    }

    public static ParameterizedTypeName multiOf(final TypeName type) {
        return ParameterizedTypeName.get(MULTI, type);
    }

    public static ParameterizedTypeName fluxOf(final TypeName type) {
        return ParameterizedTypeName.get(FLUX, type);
    }

    public static ParameterizedTypeName consumerOf(final TypeName type) {
        return ParameterizedTypeName.get(CONSUMER, WildcardTypeName.supertypeOf(type));
    }

    public static ParameterizedTypeName gradlePropertyOf(final TypeName type) {
        return ParameterizedTypeName.get(GRADLE_PROPERTY, type);
    }

    public static ParameterizedTypeName gradleListPropertyOf(final TypeName type) {
        return ParameterizedTypeName.get(GRADLE_LIST_PROPERTY, type);
    }

    public static ParameterizedTypeName gradleContainerOf(final TypeName type) {
        return ParameterizedTypeName.get(GRADLE_CONTAINERS, type);
    }

    private TypicalTypes() {
        // constants class
    }

}
