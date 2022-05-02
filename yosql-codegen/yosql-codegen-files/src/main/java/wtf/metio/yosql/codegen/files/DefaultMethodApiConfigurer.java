/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import static wtf.metio.yosql.models.constants.sql.SqlType.READING;
import static wtf.metio.yosql.models.constants.sql.SqlType.WRITING;

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
        adapted = mutiny(adapted);
        adapted = reactor(adapted);
        adapted = rxJava(adapted);
        adapted = streamEager(adapted);
        adapted = streamLazy(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration batch(final SqlConfiguration configuration) {
        if (READING == configuration.type()) {
            // TODO: allow batched select statements
            return SqlConfiguration.copyOf(configuration).withGenerateBatchApi(false);
        }
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

    // visible for testing
    SqlConfiguration mutiny(final SqlConfiguration configuration) {
        if (configuration.generateMutinyApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withGenerateMutinyApi(repositories.generateMutinyApi());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration reactor(final SqlConfiguration configuration) {
        if (configuration.generateReactorApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withGenerateReactorApi(repositories.generateReactorApi());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration rxJava(final SqlConfiguration configuration) {
        if (configuration.generateRxJavaApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withGenerateRxJavaApi(repositories.generateRxJavaApi());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamEager(final SqlConfiguration configuration) {
        if (WRITING == configuration.type()) {
            // TODO: allow eagerly streamed insert/update statements
            return SqlConfiguration.copyOf(configuration).withGenerateStreamEagerApi(false);
        }
        if (configuration.generateStreamEagerApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateStreamEagerApi(repositories.generateStreamEagerApi());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamLazy(final SqlConfiguration configuration) {
        if (WRITING == configuration.type()) {
            // TODO: allow lazily streamed insert/update statements
            return SqlConfiguration.copyOf(configuration).withGenerateStreamLazyApi(false);
        }
        if (configuration.generateStreamLazyApi().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateStreamLazyApi(repositories.generateStreamLazyApi());
        }
        return configuration;
    }

}
