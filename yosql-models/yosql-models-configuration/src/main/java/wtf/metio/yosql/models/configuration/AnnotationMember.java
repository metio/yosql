/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

/**
 * Configuration for annotation members that are placed on a constructor or method in generated code.
 */
@Value.Immutable
@JsonSerialize(
        as = ImmutableAnnotationMember.class
)
@JsonDeserialize(
        as = ImmutableAnnotationMember.class
)
public interface AnnotationMember {

    //region builders

    static ImmutableAnnotationMember.KeyBuildStage builder() {
        return ImmutableAnnotationMember.builder();
    }

    static ImmutableAnnotationMember copyOf(final AnnotationMember member) {
        return ImmutableAnnotationMember.copyOf(member);
    }

    //endregion

    /**
     * @return The key or name of the annotation member.
     */
    String key();

    /**
     * @return The value of the annotation member.
     */
    String value();

    /**
     * @return The fully-qualified type of the annotation member.
     */
    @Value.Default
    default String type() {
        return "java.lang.String";
    }

}
