/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

public final class DefaultMethodApiConfigurer implements MethodApiConfigurer {

    private final RepositoriesConfiguration repositories;

    public DefaultMethodApiConfigurer(final RepositoriesConfiguration repositories) {
        this.repositories = repositories;
    }

    @Override
    public SqlConfiguration configureApis(final SqlConfiguration configuration) {
        var adapted = configuration;
        adapted = batch(adapted);
        adapted = blocking(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration batch(final SqlConfiguration configuration) {
        if (configuration.generateBatchApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withGenerateBatchApi(repositories.generateBatchApi());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration blocking(final SqlConfiguration configuration) {
        if (configuration.generateBlockingApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withGenerateBlockingApi(repositories.generateBlockingApi());
        }
        return configuration;
    }

}
