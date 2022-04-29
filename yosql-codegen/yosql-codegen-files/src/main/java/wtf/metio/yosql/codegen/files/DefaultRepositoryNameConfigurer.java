/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.RepositoryLifecycle;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.io.File;
import java.nio.file.Path;

public final class DefaultRepositoryNameConfigurer implements RepositoryNameConfigurer {

    private final LocLogger logger;
    private final FilesConfiguration files;
    private final RepositoriesConfiguration repositories;

    public DefaultRepositoryNameConfigurer(
            final LocLogger logger,
            final FilesConfiguration files,
            final RepositoriesConfiguration repositories) {
        this.logger = logger;
        this.files = files;
        this.repositories = repositories;
    }

    @Override
    public SqlConfiguration configureName(final SqlConfiguration configuration, final Path source) {
        return SqlConfiguration.copyOf(configuration)
                .withRepository(repositoryName(configuration, source));
    }

    // visible for testing
    String repositoryName(final SqlConfiguration configuration, final Path source) {
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_INPUT, files.inputBaseDirectory());
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_SOURCE, source);
        final var relativePathToSqlFile = files.inputBaseDirectory().relativize(source);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RELATIVE, relativePathToSqlFile);
        final var rawRepositoryName = relativePathToSqlFile.getParent().toString();
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RAW, rawRepositoryName);
        final var dottedRepositoryName = rawRepositoryName.replace(File.separator, ".");
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_DOTTED, dottedRepositoryName);
        final var upperCaseName = upperCaseFirstLetterInLastSegment(dottedRepositoryName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_UPPER, upperCaseName);
        final var actualRepository = repositoryInBasePackage(upperCaseName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_ACTUAL, actualRepository);
        final var repository = repositoryWithNameSuffix(actualRepository);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_NAME, repository);
        final var userGivenRepository = repositoryWithNameSuffix(
                repositoryInBasePackage(configuration.repository()));
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_NAME, userGivenRepository);
        return SqlConfiguration.usingDefaults().build().repository().equals(configuration.repository())
                ? repository : userGivenRepository;
    }

    // visible for testing
    static String upperCaseFirstLetterInLastSegment(final String name) {
        if (name.contains(".")) {
            return name.substring(0, name.lastIndexOf('.') + 1)
                    + name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf('.') + 2).toUpperCase()
                    + name.substring(name.lastIndexOf('.') + 2);
        }
        return Strings.upperCase(name);
    }

    // visible for testing
    String repositoryWithNameSuffix(final String name) {
        return name.endsWith(repositories.repositoryNameSuffix())
                ? name
                : name + repositories.repositoryNameSuffix();
    }

    // visible for testing
    String repositoryInBasePackage(final String name) {
        return name.startsWith(repositories.basePackageName())
                ? name
                : repositories.basePackageName() + "." + name;
    }

}
