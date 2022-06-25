/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
