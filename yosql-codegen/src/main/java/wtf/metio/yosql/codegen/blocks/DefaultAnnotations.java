/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import de.xn__ho_hia.javapoet.TypeGuesser;
import wtf.metio.yosql.models.configuration.Annotation;
import wtf.metio.yosql.models.configuration.AnnotationMember;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationApis;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationMembers;
import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.EnumSet;

public final class DefaultAnnotations implements Annotations {

    private static final EnumSet<GeneratedAnnotationMembers> OPTIONS_WITH_VALUE = EnumSet.of(
            GeneratedAnnotationMembers.ALL,
            GeneratedAnnotationMembers.VALUE,
            GeneratedAnnotationMembers.WITHOUT_DATE);

    private static final EnumSet<GeneratedAnnotationMembers> OPTIONS_WITH_DATE = EnumSet.of(
            GeneratedAnnotationMembers.ALL,
            GeneratedAnnotationMembers.DATE);

    private static final EnumSet<GeneratedAnnotationMembers> OPTIONS_WITH_COMMENT = EnumSet.of(
            GeneratedAnnotationMembers.ALL,
            GeneratedAnnotationMembers.COMMENT,
            GeneratedAnnotationMembers.WITHOUT_DATE);

    private final AnnotationsConfiguration annotations;

    public DefaultAnnotations(final AnnotationsConfiguration annotations) {
        this.annotations = annotations;
    }

    @Override
    public Iterable<AnnotationSpec> generatedClass() {
        return getAnnotationSpecs(
                annotations.annotateClasses(),
                annotations.annotationApi(),
                annotations.classMembers(),
                annotations.classComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedField() {
        return getAnnotationSpecs(
                annotations.annotateFields(),
                annotations.annotationApi(),
                annotations.fieldMembers(),
                annotations.fieldComment());
    }

    @Override
    public Iterable<AnnotationSpec> generatedMethod() {
        return getAnnotationSpecs(
                annotations.annotateMethods(),
                annotations.annotationApi(),
                annotations.methodMembers(),
                annotations.methodComment());
    }

    private Iterable<AnnotationSpec> getAnnotationSpecs(
            final boolean shouldAnnotate,
            final GeneratedAnnotationApis annotationApi,
            final GeneratedAnnotationMembers memberOption,
            final String comment) {
        if (shouldAnnotate) {
            final var annotationClass = ClassName.bestGuess(annotationApi.annotationClass);
            final var builder = AnnotationSpec.builder(annotationClass);
            if (OPTIONS_WITH_VALUE.contains(memberOption)) {
                builder.addMember("value", "$S", annotations.generatorName());
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

    @Override
    public Iterable<AnnotationSpec> generatedRepository() {
        return annotations.repositoryAnnotations().stream()
                .map(DefaultAnnotations::asAnnotationSpec)
                .toList();
    }

    @Override
    public Iterable<AnnotationSpec> generatedMethod(final SqlConfiguration configuration) {
        return Annotation.mergeAnnotations(configuration.annotations(), annotations.methodAnnotations()).stream()
                .map(DefaultAnnotations::asAnnotationSpec)
                .toList();
    }

    @Override
    public Iterable<AnnotationSpec> generatedConstructor() {
        return annotations.constructorAnnotations().stream()
                .map(DefaultAnnotations::asAnnotationSpec)
                .toList();
    }

    // visible for testing
    static AnnotationSpec asAnnotationSpec(final Annotation annotation) {
        // TODO: catch exception thrown by bestGuess and return Optional + use ExecutionErrors?
        final var type = ClassName.bestGuess(annotation.type());
        final var builder = AnnotationSpec.builder(type);
        annotation.members().forEach(member -> builder.addMember(member.key(), asAnnotationMemberSpec(member)));
        return builder.build();
    }

    private static CodeBlock asAnnotationMemberSpec(final AnnotationMember member) {
        final var builder = CodeBlock.builder();
        final var type = TypeGuesser.guessTypeName(member.type());
        if (TypeName.CHAR.equals(type)) {
            builder.add("'$L'", member.value());
        } else if (type.isPrimitive()) {
            builder.add("$L", member.value());
        } else {
            builder.add("$S", member.value());
        }
        return builder.build();
    }

}
