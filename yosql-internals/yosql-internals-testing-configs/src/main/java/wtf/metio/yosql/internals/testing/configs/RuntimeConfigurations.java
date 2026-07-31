/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Object mother for {@link RuntimeConfiguration}s.
 */
public final class RuntimeConfigurations {

    public static RuntimeConfiguration defaults() {
        return RuntimeConfiguration.builder().build();
    }

    private RuntimeConfigurations() {
        // factory class
    }

}
