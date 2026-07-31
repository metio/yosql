/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
        adapted = once(adapted);
        adapted = batch(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration once(final SqlConfiguration configuration) {
        if (configuration.executeOnce().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withExecuteOnce(repositories.executeOnce());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration batch(final SqlConfiguration configuration) {
        if (configuration.executeBatch().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withExecuteBatch(repositories.executeBatch());
        }
        return configuration;
    }

}
