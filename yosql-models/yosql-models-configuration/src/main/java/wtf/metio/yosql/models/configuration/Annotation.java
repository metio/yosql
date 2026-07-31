/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import wtf.metio.yosql.internals.jdk.Buckets;
import wtf.metio.yosql.internals.jdk.Strings;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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

    static Annotation fromString(final String input) {
        return Optional.ofNullable(input)
                .map(String::strip)
                .filter(Predicate.not(Strings::isBlank))
                .map(value -> value.split(":"))
                .filter(values -> values.length > 0)
                .map(values -> Annotation.builder()
                        .setType(values[0])
                        .addAllMembers(parseMembers(values.length > 1 ? Arrays.copyOfRange(values, 1, values.length) : new String[]{}))
                        .build())
                .orElse(null);
    }

    private static List<? extends AnnotationMember> parseMembers(final String[] input) {
        return Arrays.stream(input)
                .map(String::strip)
                .filter(Predicate.not(Strings::isBlank))
                .map(value -> value.split("\\|"))
                .filter(values -> values.length > 1)
                .map(values -> AnnotationMember.builder()
                        .setKey(values[0])
                        .setValue(values[1])
                        .setType(values.length > 2 ? values[2] : "java.lang.String")
                        .build())
                .toList();
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
