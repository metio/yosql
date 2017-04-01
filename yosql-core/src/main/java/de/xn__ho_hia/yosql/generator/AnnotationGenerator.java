/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.inject.Inject;

import com.squareup.javapoet.AnnotationSpec;

import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

@SuppressWarnings({ "nls", "javadoc" })
public class AnnotationGenerator {

    private final ExecutionConfiguration config;

    @Inject
    public AnnotationGenerator(final ExecutionConfiguration config) {
        this.config = config;
    }

    public Iterable<AnnotationSpec> generatedClass(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (config.classGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    public Iterable<AnnotationSpec> generatedField(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (config.fieldGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    public Iterable<AnnotationSpec> generatedMethod(final Class<?> generatorClass) {
        final List<AnnotationSpec> annotations = new ArrayList<>(1);
        if (config.methodGeneratedAnnotation()) {
            annotations.add(generated(generatorClass));
        }
        return annotations;
    }

    private AnnotationSpec generated(final Class<?> generatorClass) {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", generatorClass.getName())
                .addMember("date", "$S", ZonedDateTime.now().toString())
                .addMember("comments", "$S", config.generatedAnnotationComment())
                .build();
    }

}
