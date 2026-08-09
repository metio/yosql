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

    /**
     * One method per statement, in whichever shape {@code createConnection} asks for, so that a test
     * about that setting sees only what it configures.
     */
    public static RepositoriesConfiguration injectConverters() {
        return RepositoriesConfiguration.copyOf(defaults()).withInjectConverters(true);
    }

    public static RepositoriesConfiguration withoutConnectionOverloads() {
        return RepositoriesConfiguration.copyOf(defaults())
                .withGenerateConnectionOverloads(false);
    }

    private RepositoriesConfigurations() {
        // factory class
    }

}
