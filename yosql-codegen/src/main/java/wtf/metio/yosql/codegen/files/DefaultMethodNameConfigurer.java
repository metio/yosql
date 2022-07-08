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
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.SourceVersion;
import java.util.Optional;

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
        return SqlConfiguration.copyOf(configuration)
                .withName(configuration.name()
                        .filter(SourceVersion::isName)
                        .or(() -> configuration.type()
                                .map(type -> validName(type, fileName))
                                .map(name -> calculateName(name, statementInFile)))
                        .or(() -> Optional.of(fileName)
                                .filter(SourceVersion::isName)
                                .map(name -> calculateName(name, statementInFile))));
    }

    private String validName(final SqlStatementType type, final String fileName) {
        return SourceVersion.isName(fileName) ? fileName : generateName(type);
    }

    private static String calculateName(final String name, final int statementInFile) {
        return statementInFile > 1 ? name + statementInFile : name;
    }

    private String generateName(final SqlStatementType type) {
        final var prefix = switch (type) {
            case READING -> repositories.allowedReadPrefixes().get(0);
            case WRITING -> repositories.allowedWritePrefixes().get(0);
            case CALLING -> repositories.allowedCallPrefixes().get(0);
        };
        return prefix + "NameWasChanged";
    }

    // visible for testing
    SqlConfiguration affixes(final SqlConfiguration configuration) {
        var adapted = configuration;
        adapted = executeOncePrefix(adapted);
        adapted = executeOnceSuffix(adapted);
        adapted = executeBatchPrefix(adapted);
        adapted = executeBatchSuffix(adapted);
        adapted = executeManyPrefix(adapted);
        adapted = executeManySuffix(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration executeOncePrefix(final SqlConfiguration configuration) {
        return configuration.executeOncePrefix()
                .filter(not(Strings::isBlank))
                .map(prefix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_ONCE_PREFIX_CHANGED, repositories.executeOncePrefix());
                    return SqlConfiguration.copyOf(configuration).withExecuteOncePrefix(repositories.executeOncePrefix());
                });
    }

    // visible for testing
    SqlConfiguration executeOnceSuffix(final SqlConfiguration configuration) {
        return configuration.executeOnceSuffix()
                .filter(not(Strings::isBlank))
                .map(suffix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_ONCE_SUFFIX_CHANGED, repositories.executeOnceSuffix());
                    return SqlConfiguration.copyOf(configuration).withExecuteOnceSuffix(repositories.executeOnceSuffix());
                });
    }

    // visible for testing
    SqlConfiguration executeBatchPrefix(final SqlConfiguration configuration) {
        return configuration.executeBatchPrefix()
                .filter(not(Strings::isBlank))
                .map(prefix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_BATCH_PREFIX_CHANGED, repositories.executeBatchPrefix());
                    return SqlConfiguration.copyOf(configuration).withExecuteBatchPrefix(repositories.executeBatchPrefix());
                });
    }

    // visible for testing
    SqlConfiguration executeBatchSuffix(final SqlConfiguration configuration) {
        return configuration.executeBatchSuffix()
                .filter(not(Strings::isBlank))
                .map(suffix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_BATCH_SUFFIX_CHANGED, repositories.executeBatchSuffix());
                    return SqlConfiguration.copyOf(configuration).withExecuteBatchSuffix(repositories.executeBatchSuffix());
                });
    }

    // visible for testing
    SqlConfiguration executeManyPrefix(final SqlConfiguration configuration) {
        return configuration.executeManyPrefix()
                .filter(not(Strings::isBlank))
                .map(prefix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_BATCH_PREFIX_CHANGED, repositories.executeManyPrefix());
                    return SqlConfiguration.copyOf(configuration).withExecuteManyPrefix(repositories.executeManyPrefix());
                });
    }

    // visible for testing
    SqlConfiguration executeManySuffix(final SqlConfiguration configuration) {
        return configuration.executeManySuffix()
                .filter(not(Strings::isBlank))
                .map(suffix -> configuration)
                .orElseGet(() -> {
                    logger.debug(SqlConfigurationLifecycle.EXECUTE_BATCH_SUFFIX_CHANGED, repositories.executeManySuffix());
                    return SqlConfiguration.copyOf(configuration).withExecuteManySuffix(repositories.executeManySuffix());
                });
    }

}
