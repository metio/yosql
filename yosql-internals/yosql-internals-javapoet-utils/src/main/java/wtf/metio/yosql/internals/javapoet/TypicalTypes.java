/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.internals.javapoet;

import com.squareup.javapoet.*;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

public final class TypicalTypes {

    public static final ClassName INJECT = ClassName.get("javax.inject", "Inject");

    public static final ClassName MAVEN_PARAMETER = ClassName.get("org.apache.maven.plugins.annotations", "Parameter");

    public static final ClassName GRADLE_PROPERTY = ClassName.get("org.gradle.api.provider", "Property");
    public static final ClassName GRADLE_LIST_PROPERTY = ClassName.get("org.gradle.api.provider", "ListProperty");
    public static final ClassName GRADLE_INPUT = ClassName.get("org.gradle.api.tasks", "Input");
    public static final ClassName GRADLE_INPUT_DIRECTORY = ClassName.get("org.gradle.api.tasks", "InputDirectory");
    public static final ClassName GRADLE_OUTPUT_DIRECTORY = ClassName.get("org.gradle.api.tasks", "OutputDirectory");
    public static final ClassName GRADLE_CONTAINERS = ClassName.get("org.gradle.api", "NamedDomainObjectContainer");
    public static final ClassName GRADLE_DIRECTORY = ClassName.get("org.gradle.api.file", "DirectoryProperty");
    public static final ClassName GRADLE_LAYOUT = ClassName.get("org.gradle.api.file", "ProjectLayout");
    public static final ClassName GRADLE_OBJECTS = ClassName.get("org.gradle.api.model", "ObjectFactory");
    public static final ClassName GRADLE_NAMED = ClassName.get("org.gradle.api", "Named");

    public static final TypeName ARRAY_OF_INTS = ArrayTypeName.of(int.class);

    public static final TypeName MAP_OF_STRING_AND_ARRAY_OF_INTS = mapOf(ClassName.get(String.class), ARRAY_OF_INTS);
    public static final TypeName MAP_OF_STRING_AND_OBJECTS = mapOf(ClassName.get(String.class), TypeName.OBJECT);
    public static final TypeName MAP_OF_STRING_AND_LONGS = mapOf(ClassName.get(String.class), ClassName.get(Long.class));
    public static final TypeName LINKED_MAP_OF_STRING_AND_OBJECTS = linkedMapOf(ClassName.get(String.class), TypeName.OBJECT);

    public static ParameterizedTypeName mapOf(final TypeName key, final TypeName value) {
        return ParameterizedTypeName.get(ClassName.get(Map.class), key, value);
    }

    public static ParameterizedTypeName linkedMapOf(final TypeName key, final TypeName value) {
        return ParameterizedTypeName.get(ClassName.get(LinkedHashMap.class), key, value);
    }

    public static ParameterizedTypeName optionalOf(final Type type) {
        return optionalOf(TypeName.get(type));
    }

    public static ParameterizedTypeName optionalOf(final TypeName type) {
        return ParameterizedTypeName.get(ClassName.get(Optional.class), type);
    }

    public static ParameterizedTypeName supplierOf(final TypeName type) {
        return ParameterizedTypeName.get(ClassName.get(Supplier.class), type);
    }

    public static ParameterizedTypeName listOf(final Type type) {
        return listOf(TypeName.get(type));
    }

    public static ParameterizedTypeName listOf(final TypeName type) {
        return ParameterizedTypeName.get(ClassName.get(List.class), type);
    }

    public static ParameterizedTypeName streamOf(final TypeName type) {
        return ParameterizedTypeName.get(ClassName.get(Stream.class), type);
    }

    public static ParameterizedTypeName consumerOf(final TypeName type) {
        return ParameterizedTypeName.get(ClassName.get(Consumer.class), WildcardTypeName.supertypeOf(type));
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
