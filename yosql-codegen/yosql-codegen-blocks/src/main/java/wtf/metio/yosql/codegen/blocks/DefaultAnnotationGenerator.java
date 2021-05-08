/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.codegen.api.AnnotationGenerator;
import wtf.metio.yosql.models.constants.api.AnnotationApis;
import wtf.metio.yosql.models.constants.configuration.AnnotationMembers;
import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;
import wtf.metio.yosql.models.immutables.ApiConfiguration;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.EnumSet;

public final class DefaultAnnotationGenerator implements AnnotationGenerator {

    private static final EnumSet<AnnotationMembers> OPTIONS_WITH_VALUE = EnumSet.of(
            AnnotationMembers.ALL,
            AnnotationMembers.VALUE,
            AnnotationMembers.WITHOUT_DATE);

    private static final EnumSet<AnnotationMembers> OPTIONS_WITH_DATE = EnumSet.of(
            AnnotationMembers.ALL,
            AnnotationMembers.DATE);

    private static final EnumSet<AnnotationMembers> OPTIONS_WITH_COMMENT = EnumSet.of(
            AnnotationMembers.ALL,
            AnnotationMembers.COMMENT,
            AnnotationMembers.WITHOUT_DATE);

    private final AnnotationsConfiguration configuration;
    private final ApiConfiguration api;

    public DefaultAnnotationGenerator(final AnnotationsConfiguration configuration, final ApiConfiguration api) {
        this.configuration = configuration;
        this.api = api;
    }

    @Override
    public Iterable<AnnotationSpec> generatedClass() {
        return getAnnotationSpecs(
                configuration.annotateClasses(),
                api.annotationApi(),
                configuration.classMembers(),
                configuration.classComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedField() {
        return getAnnotationSpecs(
                configuration.annotateFields(),
                api.annotationApi(),
                configuration.fieldMembers(),
                configuration.fieldComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedMethod() {
        return getAnnotationSpecs(
                configuration.annotateMethods(),
                api.annotationApi(),
                configuration.methodMembers(),
                configuration.methodComment());
    }

    private Iterable<AnnotationSpec> getAnnotationSpecs(
            final boolean shouldAnnotate,
            final AnnotationApis annotationApi,
            final AnnotationMembers memberOption,
            final String comment) {
        if (shouldAnnotate) {
            final var annotationClass = ClassName.bestGuess(annotationApi.annotationClass);
            final var builder = AnnotationSpec.builder(annotationClass);
            if (OPTIONS_WITH_VALUE.contains(memberOption)) {
                builder.addMember("value", "$S", configuration.generatorName());
            }
            if (OPTIONS_WITH_DATE.contains(memberOption)) {
                builder.addMember("date", "$S", ZonedDateTime.now().toString());
            }
            if (OPTIONS_WITH_COMMENT.contains(memberOption)) {
                builder.addMember("comments", "$S", comment);
            }
            return Collections.singletonList(builder.build());
        }
        return Collections.emptyList();
    }

}
