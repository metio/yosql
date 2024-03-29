/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.List;
import java.util.Optional;

import static wtf.metio.yosql.models.configuration.SqlStatementType.*;

public final class DefaultMethodSettingsConfigurer implements MethodSettingsConfigurer {

    private final RepositoriesConfiguration repositories;

    public DefaultMethodSettingsConfigurer(final RepositoriesConfiguration repositories) {
        this.repositories = repositories;
    }

    @Override
    public SqlConfiguration configureSettings(final SqlConfiguration configuration) {
        var adapted = configuration;
        adapted = type(adapted);
        adapted = returningMode(adapted);
        adapted = catchAndRethrow(adapted);
        adapted = throwOnMultipleResults(adapted);
        adapted = usePreparedStatement(adapted);
        adapted = writesReturnUpdateCount(adapted);
        adapted = createConnection(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration type(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withType(configuration.type()
                        .or(() -> mapNameToType(configuration.name().orElse(""))));
    }

    private Optional<SqlStatementType> mapNameToType(final String name) {
        if (startsWith(name, repositories.allowedWritePrefixes())) {
            return Optional.of(WRITING);
        } else if (startsWith(name, repositories.allowedReadPrefixes())) {
            return Optional.of(READING);
        } else if (startsWith(name, repositories.allowedCallPrefixes())) {
            return Optional.of(CALLING);
        }
        return Optional.empty();
    }

    private static boolean startsWith(final String fileName, final List<String> prefixes) {
        return prefixes != null && prefixes.stream().anyMatch(fileName::startsWith);
    }

    // visible for testing
    static SqlConfiguration returningMode(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withReturningMode(configuration.returningMode()
                        .or(() -> configuration.type().map(DefaultMethodSettingsConfigurer::mapTypeReturningMode)));
    }

    private static ReturningMode mapTypeReturningMode(final SqlStatementType type) {
        return switch (type) {
            case CALLING -> ReturningMode.SINGLE;
            case READING -> ReturningMode.MULTIPLE;
            case WRITING -> ReturningMode.NONE;
        };
    }

    // visible for testing
    SqlConfiguration catchAndRethrow(final SqlConfiguration configuration) {
        if (configuration.catchAndRethrow().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withCatchAndRethrow(repositories.catchAndRethrow());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration throwOnMultipleResults(final SqlConfiguration configuration) {
        if (configuration.throwOnMultipleResults().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withThrowOnMultipleResults(repositories.throwOnMultipleResults());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration usePreparedStatement(final SqlConfiguration configuration) {
        if (configuration.usePreparedStatement().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withUsePreparedStatement(repositories.usePreparedStatement());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration writesReturnUpdateCount(final SqlConfiguration configuration) {
        if (configuration.writesReturnUpdateCount().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withWritesReturnUpdateCount(repositories.writesReturnUpdateCount());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration createConnection(final SqlConfiguration configuration) {
        if (configuration.createConnection().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withCreateConnection(repositories.createConnection());
        }
        return configuration;
    }

}
