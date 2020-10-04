/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

@Value.Immutable
public interface AnnotationConfiguration {

    static ImmutableAnnotationConfiguration.Builder builder() {
        return ImmutableAnnotationConfiguration.builder();
    }

    static ImmutableAnnotationConfiguration copy(final AnnotationConfiguration configuration) {
        return ImmutableAnnotationConfiguration.copyOf(configuration);
    }

    AnnotationClassOptions classAnnotation();

    AnnotationClassOptions fieldAnnotation();

    AnnotationClassOptions methodAnnotation();

    AnnotationMemberOptions classMembers();

    AnnotationMemberOptions fieldMembers();

    AnnotationMemberOptions methodMembers();

    String classComment();

    String fieldComment();

    String methodComment();

    String generatorName();

}
