package com.github.sebhoss.yosql.generator;

import java.time.ZonedDateTime;

import javax.annotation.Generated;
import javax.inject.Named;
import javax.inject.Singleton;

import com.squareup.javapoet.AnnotationSpec;

@Named
@Singleton
public class CommonGenerator {

    public AnnotationSpec generatedAnnotation(final Class<?> generatorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", generatorClass.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", "DO NOT EDIT")
                .build();
    }

}
