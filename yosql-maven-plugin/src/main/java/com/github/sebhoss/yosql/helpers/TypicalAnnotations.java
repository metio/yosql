package com.github.sebhoss.yosql.helpers;

import java.time.ZonedDateTime;

import javax.annotation.Generated;

import com.squareup.javapoet.AnnotationSpec;

public final class TypicalAnnotations {

    public static final AnnotationSpec generatedAnnotation(final Class<?> generatorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", generatorClass.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", "DO NOT EDIT")
                .build();
    }

}
