/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.JavaConfiguration;

/**
 * Presets for {@link JavaConfiguration}s.
 */
public final class JavaConfigurations {

    /**
     * @return Preset using the user visible defaults.
     */
    public static JavaConfiguration defaults() {
        return JavaConfiguration.builder().build();
    }

    private JavaConfigurations() {
        // factory class
    }

}

