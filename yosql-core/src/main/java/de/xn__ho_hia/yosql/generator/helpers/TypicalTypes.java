/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.helpers;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

@SuppressWarnings({ "nls", "javadoc" })
public class TypicalTypes {

    public static final ClassName OBJECT                          = ClassName.get("java.lang", "Object");
    public static final ClassName STRING                          = ClassName.get("java.lang", "String");
    public static final ClassName INTEGER                         = ClassName.get("java.lang", "Integer");

    public static final ClassName COLLECTION                      = ClassName.get("java.util", "Collection");
    public static final ClassName LIST                            = ClassName.get("java.util", "List");
    public static final ClassName MAP                             = ClassName.get("java.util", "Map");
    public static final ClassName STREAM                          = ClassName.get("java.util.stream", "Stream");
    public static final ClassName ARRAY_LIST                      = ClassName.get("java.util", "ArrayList");
    public static final ClassName CONSUMER                        = ClassName.get("java.util.function", "Consumer");

    public static final ClassName DATE                            = ClassName.get("java.sql", "Date");
    public static final ClassName TIME                            = ClassName.get("java.sql", "Time");
    public static final ClassName TIMESTAMP                       = ClassName.get("java.sql", "Timestamp");

    public static final ClassName INPUT_STREAM                    = ClassName.get("java.io", "InputStream");
    public static final ClassName READER                          = ClassName.get("java.io", "Reader");

    public static final ClassName BIG_DECIMAL                     = ClassName.get("java.math", "BigDecimal");

    public static final ClassName PATTERN                         = ClassName.get("java.util.regex", "Pattern");
    public static final ClassName MATCHER                         = ClassName.get("java.util.regex", "Matcher");

    public static final ClassName FLOWABLE                        = ClassName.get("io.reactivex", "Flowable");

    public static final TypeName  ARRAY_OF_INTS                   = ArrayTypeName.of(int.class);
    public static final TypeName  ARRAY_OF_BYTES                  = ArrayTypeName.of(byte.class);
    public static final TypeName  LIST_OF_STRINGS                 = ParameterizedTypeName.get(LIST, STRING);
    public static final TypeName  COLLECTION_OF_INTEGERS          = ParameterizedTypeName.get(COLLECTION, INTEGER);
    public static final TypeName  MAP_OF_STRING_AND_ARRAY_OF_INTS = ParameterizedTypeName.get(MAP, STRING,
            ARRAY_OF_INTS);
    public static final TypeName  MAP_OF_STRING_AND_OBJECTS       = ParameterizedTypeName.get(MAP, STRING, OBJECT);

    public static final TypeName  LIST_OF_MAPS                    = ParameterizedTypeName.get(LIST,
            TypicalTypes.MAP_OF_STRING_AND_OBJECTS);
    public static final TypeName  STREAM_OF_MAPS                  = ParameterizedTypeName.get(STREAM,
            TypicalTypes.MAP_OF_STRING_AND_OBJECTS);
    public static final TypeName  FLOWABLE_OF_MAPS                = ParameterizedTypeName.get(FLOWABLE,
            TypicalTypes.MAP_OF_STRING_AND_OBJECTS);

    public static final ParameterizedTypeName listOf(final TypeName type) {
        return ParameterizedTypeName.get(LIST, type);
    }

    public static final ParameterizedTypeName streamOf(final TypeName type) {
        return ParameterizedTypeName.get(STREAM, type);
    }

    public static final ParameterizedTypeName consumerOf(final TypeName type) {
        return ParameterizedTypeName.get(CONSUMER, WildcardTypeName.supertypeOf(type));
    }

    // TODO: remove?
    public static final TypeSpec.Builder publicClass(final String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(TypicalModifiers.PUBLIC_CLASS);
    }

    public static final TypeSpec.Builder publicClass(final ClassName name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(TypicalModifiers.PUBLIC_CLASS);
    }

    // TODO remove?
    public static final TypeSpec.Builder openClass(final String name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(TypicalModifiers.OPEN_CLASS);
    }

    public static final TypeSpec.Builder openClass(final ClassName name) {
        return TypeSpec.classBuilder(name)
                .addModifiers(TypicalModifiers.OPEN_CLASS);
    }

}
