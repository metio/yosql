/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.helpers;

import com.squareup.javapoet.*;

public final class TypicalTypes {

    public static final ClassName OBJECT = ClassName.get("java.lang", "Object");
    public static final ClassName STRING = ClassName.get("java.lang", "String");

    private static final ClassName LIST = ClassName.get("java.util", "List");
    private static final ClassName MAP = ClassName.get("java.util", "Map");
    private static final ClassName STREAM = ClassName.get("java.util.stream", "Stream");
    private static final ClassName CONSUMER = ClassName.get("java.util.function", "Consumer");

    public static final ClassName FLOWABLE = ClassName.get("io.reactivex", "Flowable");

    public static final TypeName ARRAY_OF_INTS = ArrayTypeName.of(int.class);

    public static final TypeName MAP_OF_STRING_AND_ARRAY_OF_INTS = mapOf(STRING, ARRAY_OF_INTS);
    public static final TypeName MAP_OF_STRING_AND_OBJECTS = mapOf(STRING, OBJECT);

    public static ParameterizedTypeName mapOf(final TypeName key, final TypeName value) {
        return ParameterizedTypeName.get(MAP, key, value);
    }

    public static ParameterizedTypeName listOf(final TypeName type) {
        return ParameterizedTypeName.get(LIST, type);
    }

    public static ParameterizedTypeName streamOf(final TypeName type) {
        return ParameterizedTypeName.get(STREAM, type);
    }

    public static ParameterizedTypeName consumerOf(final TypeName type) {
        return ParameterizedTypeName.get(CONSUMER, WildcardTypeName.supertypeOf(type));
    }

    private TypicalTypes() {
        // constants class
    }

}
