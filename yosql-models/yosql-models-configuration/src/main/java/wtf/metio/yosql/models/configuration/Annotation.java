/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.jdk.Buckets;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration for an extra annotation that is to be placed on a constructor or method in generated code.
 */
@Value.Immutable
@JsonSerialize(
        as = ImmutableAnnotation.class
)
@JsonDeserialize(
        as = ImmutableAnnotation.class
)
public interface Annotation {

    //region builders

    static ImmutableAnnotation.TypeBuildStage builder() {
        return ImmutableAnnotation.builder();
    }

    static ImmutableAnnotation copyOf(final Annotation annotation) {
        return ImmutableAnnotation.copyOf(annotation);
    }

    //endregion

    //region utils

    static List<Annotation> mergeAnnotations(
            final List<Annotation> first,
            final List<Annotation> second) {
        if (first == null || first.isEmpty()) {
            return second;
        }
        return Stream.concat(copyAttributes(first, second), copyAttributes(second, first))
                .filter(Buckets.distinctByKey(Annotation::type))
                .collect(Collectors.toList());
    }

    private static Stream<ImmutableAnnotation> copyAttributes(
            final List<Annotation> first,
            final List<Annotation> second) {
        return first.stream()
                .map(annotation -> second.stream()
                        .filter(other -> annotation.type().equals(other.type()))
                        .findFirst()
                        .map(other -> Annotation.copyOf(annotation)
                                .withMembers(mergeMembers(annotation.members(), other.members())))
                        .orElseGet(() -> Annotation.copyOf(annotation)));
    }

    private static List<AnnotationMember> mergeMembers(
            final List<AnnotationMember> first,
            final List<AnnotationMember> second) {
        if (first == null || first.isEmpty()) {
            return second;
        }
        return Stream.concat(first.stream(), second.stream())
                .filter(Buckets.distinctByKey(AnnotationMember::key))
                .collect(Collectors.toList());
    }

    //endregion

    /**
     * @return The fully-qualified type name of this annotation.
     */
    String type();

    /**
     * @return The members of this annotation.
     */
    List<AnnotationMember> members();

}
