/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.AnnotationsConfiguration;

/**
 * Object mother for {@link AnnotationsConfiguration}s.
 */
public final class AnnotationsConfigurations {

    public static AnnotationsConfiguration defaults() {
        return AnnotationsConfiguration.builder().build();
    }

    private AnnotationsConfigurations() {
        // factory class
    }

}
