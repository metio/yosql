/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.SqlConfigurationLifecycle;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.SourceVersion;

import static java.util.function.Predicate.not;

/**
 * Default implementation of a {@link MethodNameConfigurer}.
 */
public final class DefaultMethodNameConfigurer implements MethodNameConfigurer {

    private final LocLogger logger;
    private final RepositoriesConfiguration repositories;

    /**
     * @param logger       The logger to use.
     * @param repositories The repository configuration to use.
     */
    public DefaultMethodNameConfigurer(final LocLogger logger, final RepositoriesConfiguration repositories) {
        this.logger = logger;
        this.repositories = repositories;
    }

    @Override
    public SqlConfiguration configureNames(
            final SqlConfiguration configuration,
            final String fileName,
            final int statementInFile) {
        var adapted = configuration;
        adapted = baseName(adapted, fileName, statementInFile);
        adapted = affixes(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration baseName(final SqlConfiguration configuration, final String fileName, final int statementInFile) {
        return configuration.name()
                .filter(not(Strings::isBlank))
                .filter(SourceVersion::isName)
                .map(name -> configuration)
                .orElseGet(() -> SqlConfiguration.copyOf(configuration)
                        .withName(calculateName(validName(configuration.type().orElse(SqlType.UNKNOWN), fileName),
                                statementInFile)));
    }

    private String validName(final SqlType type, final String fileName) {
        return SourceVersion.isName(fileName) ? fileName : generateName(type);
    }

    private static String calculateName(final String name, final int statementInFile) {
        return statementInFile > 1 ? name + statementInFile : name;
    }

    private String generateName(final SqlType type) {
        final var typeLookup = switch (type) {
            case READING -> repositories.allowedReadPrefixes().get(0);
            case WRITING -> repositories.allowedWritePrefixes().get(0);
            case CALLING -> repositories.allowedCallPrefixes().get(0);
            case UNKNOWN -> "statement";
        };
        return typeLookup + "NameWasChanged";
    }

    // visible for testing
    SqlConfiguration affixes(final SqlConfiguration configuration) {
        var adapted = configuration;
        adapted = batchNamePrefix(adapted);
        adapted = batchNameSuffix(adapted);
        adapted = blockingNamePrefix(adapted);
        adapted = blockingNameSuffix(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration batchNamePrefix(final SqlConfiguration configuration) {
        return configuration.batchPrefix()
                .filter(not(Strings::isBlank))
                .map(prefix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.BATCH_PREFIX_NAME_CHANGED, repositories.batchPrefix());
                    return SqlConfiguration.copyOf(configuration).withBatchPrefix(repositories.batchPrefix());
                });
    }

    // visible for testing
    SqlConfiguration batchNameSuffix(final SqlConfiguration configuration) {
        return configuration.batchSuffix()
                .filter(not(Strings::isBlank))
                .map(suffix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.BATCH_SUFFIX_NAME_CHANGED, repositories.batchSuffix());
                    return SqlConfiguration.copyOf(configuration).withBatchSuffix(repositories.batchSuffix());
                });
    }

    // visible for testing
    SqlConfiguration blockingNamePrefix(final SqlConfiguration configuration) {
        return configuration.blockingPrefix()
                .filter(not(Strings::isBlank))
                .map(prefix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.BLOCKING_PREFIX_NAME_CHANGED, repositories.blockingPrefix());
                    return SqlConfiguration.copyOf(configuration).withBlockingPrefix(repositories.blockingPrefix());
                });
    }

    // visible for testing
    SqlConfiguration blockingNameSuffix(final SqlConfiguration configuration) {
        return configuration.blockingSuffix()
                .filter(not(Strings::isBlank))
                .map(suffix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.BLOCKING_SUFFIX_NAME_CHANGED, repositories.blockingSuffix());
                    return SqlConfiguration.copyOf(configuration).withBlockingSuffix(repositories.blockingSuffix());
                });
    }

}
