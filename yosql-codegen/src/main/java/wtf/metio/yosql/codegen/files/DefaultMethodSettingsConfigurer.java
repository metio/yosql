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
        adapted = injectConverters(adapted);
        adapted = throwOnMultipleResultsForSingle(adapted);
        adapted = usePreparedStatement(adapted);
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
    SqlConfiguration injectConverters(final SqlConfiguration configuration) {
        if (configuration.injectConverters().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withInjectConverters(repositories.injectConverters());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration throwOnMultipleResultsForSingle(final SqlConfiguration configuration) {
        if (configuration.throwOnMultipleResultsForSingle().isEmpty()) {
            return SqlConfiguration.copyOf(configuration)
                    .withThrowOnMultipleResultsForSingle(repositories.throwOnMultipleResultsForSingle());
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

}
