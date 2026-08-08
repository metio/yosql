/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.codegen.dao.InLists;
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

    /**
     * A statement that said nothing about batching takes the project's answer — unless it holds a
     * collection parameter, which no batch method could have.
     *
     * <p>Each element of a collection expands into a placeholder of its own, so every execution of
     * the batch would need a different query. Applying the project default there turns every
     * {@code update … where state in (:states)} into a build failure over a method the author never
     * asked for, and the only cure is writing {@code executeBatch: false} on each of them. A
     * statement that asks for the batch in its own front matter still gets the error: it asked for
     * something impossible, rather than being handed it.</p>
     */
    // visible for testing
    SqlConfiguration batch(final SqlConfiguration configuration) {
        if (configuration.executeBatch().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withExecuteBatch(repositories.executeBatch() && !InLists.anyExpands(configuration));
        }
        return configuration;
    }

}
