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
