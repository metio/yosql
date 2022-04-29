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
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.SourceVersion;


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
        if (Strings.isBlank(configuration.name())) {
            final var validName = SourceVersion.isName(fileName) ? fileName : generateName(configuration);
            return SqlConfiguration.copyOf(configuration)
                    .withName(calculateName(validName, statementInFile));
        }
        if (SourceVersion.isName(configuration.name())) {
            return configuration;
        }
        return SqlConfiguration.copyOf(configuration)
                .withName(calculateName(generateName(configuration), statementInFile));
    }

    private static String calculateName(final String name, final int statementInFile) {
        return statementInFile > 1 ? name + statementInFile : name;
    }

    private String generateName(final SqlConfiguration configuration) {
        final var typeLookup = switch (configuration.type()) {
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
        adapted = mutinyNamePrefix(adapted);
        adapted = mutinyNameSuffix(adapted);
        adapted = reactorNamePrefix(adapted);
        adapted = reactorNameSuffix(adapted);
        adapted = rxJavaNamePrefix(adapted);
        adapted = rxJavaNameSuffix(adapted);
        adapted = streamLazyNamePrefix(adapted);
        adapted = streamLazyNameSuffix(adapted);
        adapted = streamEagerNamePrefix(adapted);
        adapted = streamEagerNameSuffix(adapted);
        return adapted;
    }

    // visible for testing
    SqlConfiguration batchNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.batchPrefix())) {
            logger.debug(SqlConfigurationLifecycle.BATCH_PREFIX_NAME_CHANGED, repositories.batchPrefix());
            return SqlConfiguration.copyOf(configuration).withBatchPrefix(repositories.batchPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration batchNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.batchSuffix())) {
            logger.debug(SqlConfigurationLifecycle.BATCH_SUFFIX_NAME_CHANGED, repositories.batchSuffix());
            return SqlConfiguration.copyOf(configuration).withBatchSuffix(repositories.batchSuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration blockingNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.blockingPrefix())) {
            logger.debug(SqlConfigurationLifecycle.BLOCKING_PREFIX_NAME_CHANGED, repositories.blockingPrefix());
            return SqlConfiguration.copyOf(configuration).withBlockingPrefix(repositories.blockingPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration blockingNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.blockingSuffix())) {
            logger.debug(SqlConfigurationLifecycle.BLOCKING_SUFFIX_NAME_CHANGED, repositories.blockingPrefix());
            return SqlConfiguration.copyOf(configuration).withBlockingSuffix(repositories.blockingSuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration mutinyNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.mutinyPrefix())) {
            logger.debug(SqlConfigurationLifecycle.MUTINY_PREFIX_NAME_CHANGED, repositories.mutinyPrefix());
            return SqlConfiguration.copyOf(configuration).withMutinyPrefix(repositories.mutinyPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration mutinyNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.mutinySuffix())) {
            logger.debug(SqlConfigurationLifecycle.MUTINY_SUFFIX_NAME_CHANGED, repositories.mutinySuffix());
            return SqlConfiguration.copyOf(configuration).withMutinySuffix(repositories.mutinySuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration reactorNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.reactorPrefix())) {
            logger.debug(SqlConfigurationLifecycle.REACTOR_PREFIX_NAME_CHANGED, repositories.reactorPrefix());
            return SqlConfiguration.copyOf(configuration).withReactorPrefix(repositories.reactorPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration reactorNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.reactorSuffix())) {
            logger.debug(SqlConfigurationLifecycle.REACTOR_SUFFIX_NAME_CHANGED, repositories.reactorSuffix());
            return SqlConfiguration.copyOf(configuration).withReactorSuffix(repositories.reactorSuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration rxJavaNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.rxJavaPrefix())) {
            logger.debug(SqlConfigurationLifecycle.RXJAVA_PREFIX_NAME_CHANGED, repositories.rxJavaPrefix());
            return SqlConfiguration.copyOf(configuration).withRxJavaPrefix(repositories.rxJavaPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration rxJavaNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.rxJavaSuffix())) {
            logger.debug(SqlConfigurationLifecycle.RXJAVA_SUFFIX_NAME_CHANGED, repositories.rxJavaSuffix());
            return SqlConfiguration.copyOf(configuration).withRxJavaSuffix(repositories.rxJavaSuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamLazyNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.streamLazyPrefix())) {
            logger.debug(SqlConfigurationLifecycle.STREAM_LAZY_PREFIX_NAME_CHANGED, repositories.streamLazyPrefix());
            return SqlConfiguration.copyOf(configuration).withStreamLazyPrefix(repositories.streamLazyPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamLazyNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.streamLazySuffix())) {
            logger.debug(SqlConfigurationLifecycle.STREAM_LAZY_SUFFIX_NAME_CHANGED, repositories.streamLazySuffix());
            return SqlConfiguration.copyOf(configuration).withStreamLazySuffix(repositories.streamLazySuffix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamEagerNamePrefix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.streamEagerPrefix())) {
            logger.debug(SqlConfigurationLifecycle.STREAM_EAGER_PREFIX_NAME_CHANGED, repositories.streamEagerPrefix());
            return SqlConfiguration.copyOf(configuration).withStreamEagerPrefix(repositories.streamEagerPrefix());
        }
        return configuration;
    }

    // visible for testing
    SqlConfiguration streamEagerNameSuffix(final SqlConfiguration configuration) {
        if (Strings.isBlank(configuration.streamEagerSuffix())) {
            logger.debug(SqlConfigurationLifecycle.STREAM_EAGER_SUFFIX_NAME_CHANGED, repositories.streamEagerSuffix());
            return SqlConfiguration.copyOf(configuration).withStreamEagerSuffix(repositories.streamEagerSuffix());
        }
        return configuration;
    }

}
