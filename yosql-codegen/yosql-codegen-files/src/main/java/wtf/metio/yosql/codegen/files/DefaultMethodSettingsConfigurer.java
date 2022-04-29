/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.List;

import static wtf.metio.yosql.models.constants.sql.SqlType.*;

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
        return adapted;
    }

    // visible for testing
    SqlConfiguration type(final SqlConfiguration configuration) {
        if (configuration.type() == null || UNKNOWN.equals(configuration.type())) {
            return SqlConfiguration.copyOf(configuration)
                    .withType(mapNameToType(configuration.name()));
        }
        return configuration;
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
        if (configuration.returningMode() == null || configuration.returningMode() == ReturningMode.NONE) {
            return SqlConfiguration.copyOf(configuration)
                    .withReturningMode(mapTypeReturningMode(configuration.type()));
        }
        return configuration;
    }

    private ReturningMode mapTypeReturningMode(final SqlType type) {
        return switch (type) {
            case READING -> ReturningMode.LIST;
            case CALLING -> ReturningMode.FIRST;
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

}
