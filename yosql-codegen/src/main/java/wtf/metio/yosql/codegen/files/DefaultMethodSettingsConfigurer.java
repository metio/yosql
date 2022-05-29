/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.configuration.ReturningMode;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.List;

import static wtf.metio.yosql.models.configuration.SqlType.*;

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
        return configuration.type()
                .filter(type -> UNKNOWN != type)
                .map(type -> configuration)
                .orElseGet(() -> SqlConfiguration.copyOf(configuration)
                        .withType(mapNameToType(configuration.name().orElse(""))));
    }

    private SqlType mapNameToType(final String name) {
        if (startsWith(name, repositories.allowedWritePrefixes())) {
            return WRITING;
        } else if (startsWith(name, repositories.allowedReadPrefixes())) {
            return READING;
        } else if (startsWith(name, repositories.allowedCallPrefixes())) {
            return CALLING;
        }
        return UNKNOWN;
    }

    private static boolean startsWith(final String fileName, final List<String> prefixes) {
        return prefixes != null && prefixes.stream().anyMatch(fileName::startsWith);
    }

    // visible for testing
    SqlConfiguration returningMode(final SqlConfiguration configuration) {
        return configuration.returningMode()
                .map(mode -> configuration)
                .orElseGet(() -> SqlConfiguration.copyOf(configuration)
                        .withReturningMode(mapTypeReturningMode(configuration.type().orElse(UNKNOWN))));
    }

    private ReturningMode mapTypeReturningMode(final SqlType type) {
        return switch (type) {
            case READING -> ReturningMode.MULTIPLE;
            case CALLING -> ReturningMode.SINGLE;
            default -> ReturningMode.NONE;
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
