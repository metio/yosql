/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.IMessageConveyor;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.TypeName;
import wtf.metio.javapoet.TypeGuesser;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.configuration.Annotation;
import wtf.metio.yosql.models.configuration.AnnotationMember;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationApis;
import wtf.metio.yosql.models.configuration.GeneratedAnnotationMembers;
import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.time.ZonedDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static wtf.metio.yosql.codegen.lifecycle.ApplicationWarnings.CANNOT_GUESS_ANNOTATION_MEMBER_TYPE;
import static wtf.metio.yosql.codegen.lifecycle.ApplicationWarnings.CANNOT_GUESS_ANNOTATION_TYPE;

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
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    public DefaultAnnotations(
            final AnnotationsConfiguration annotations,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        this.annotations = annotations;
        this.errors = errors;
        this.messages = messages;
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
            return List.of(builder.build());
        }
        return List.of();
    }

    @Override
    public Iterable<AnnotationSpec> generatedRepository() {
        return annotations.repositoryAnnotations().stream()
                .flatMap(annotation -> asAnnotationSpec(annotation).stream())
                .toList();
    }

    @Override
    public Iterable<AnnotationSpec> generatedMethod(final SqlConfiguration configuration) {
        return Annotation.mergeAnnotations(configuration.annotations(), annotations.methodAnnotations()).stream()
                .flatMap(annotation -> asAnnotationSpec(annotation).stream())
                .toList();
    }

    @Override
    public Iterable<AnnotationSpec> generatedConstructor() {
        return annotations.constructorAnnotations().stream()
                .flatMap(annotation -> asAnnotationSpec(annotation).stream())
                .toList();
    }

    // visible for testing
    Optional<AnnotationSpec> asAnnotationSpec(final Annotation annotation) {
        try {
            final var type = ClassName.bestGuess(annotation.type());
            final var builder = AnnotationSpec.builder(type);
            annotation.members().forEach(member -> asAnnotationMemberSpec(member)
                    .ifPresent(code -> builder.addMember(member.key(), code)));
            return Optional.of(builder.build());
        } catch (final IllegalArgumentException exception) {
            errors.illegalArgument(exception, messages.getMessage(CANNOT_GUESS_ANNOTATION_TYPE, annotation.type()));
            return Optional.empty();
        }
    }

    private Optional<CodeBlock> asAnnotationMemberSpec(final AnnotationMember member) {
        try {
            final var builder = CodeBlock.builder();
            final var type = TypeGuesser.guessTypeName(member.type());
            if (TypeName.CHAR.equals(type)) {
                builder.add("'$L'", member.value());
            } else if (type.isPrimitive()) {
                builder.add("$L", member.value());
            } else {
                builder.add("$S", member.value());
            }
            return Optional.of(builder.build());
        } catch (final IllegalArgumentException exception) {
            errors.illegalArgument(exception, messages.getMessage(CANNOT_GUESS_ANNOTATION_MEMBER_TYPE, member.type()));
            return Optional.empty();
        }
    }

}
