/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;

/**
 * Object mother for {@link RepositoriesConfiguration}s.
 */
public final class RepositoriesConfigurations {

    public static RepositoriesConfiguration defaults() {
        return RepositoriesConfiguration.builder().build();
    }

    public static RepositoriesConfiguration validatingMethodNames() {
        return RepositoriesConfiguration.copyOf(defaults())
                .withValidateMethodNamePrefixes(true);
    }

    private RepositoriesConfigurations() {
        // factory class
    }

}
