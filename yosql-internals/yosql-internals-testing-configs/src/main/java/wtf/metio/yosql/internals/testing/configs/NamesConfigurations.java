/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.NamesConfiguration;

/**
 * Object mother for {@link NamesConfiguration}s.
 */
public final class NamesConfigurations {

    public static NamesConfiguration defaults() {
        return NamesConfiguration.builder().build();
    }

    private NamesConfigurations() {
        // factory class
    }

}
