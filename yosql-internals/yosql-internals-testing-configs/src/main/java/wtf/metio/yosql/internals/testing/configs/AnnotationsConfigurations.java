/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.configuration.GeneratedAnnotationApis;
import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;

/**
 * Object mother for {@link AnnotationsConfiguration}s.
 */
public final class AnnotationsConfigurations {

    public static AnnotationsConfiguration defaults() {
        return AnnotationsConfiguration.builder()
                .setAnnotateClasses(true)
                .setAnnotateFields(true)
                .setAnnotateMethods(true)
                .build();
    }

    public static AnnotationsConfiguration generated() {
        return AnnotationsConfiguration.copyOf(defaults())
                .withAnnotationApi(GeneratedAnnotationApis.ANNOTATION_API);
    }

    private AnnotationsConfigurations() {
        // factory class
    }

}
