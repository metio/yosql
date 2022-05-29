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
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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
    public SqlConfiguration configureNames(final SqlConfiguration configuration, final Path source) {
        return SqlConfiguration.copyOf(configuration)
                .withRepository(repositoryClassName(configuration, source))
                .withRepositoryInterface(repositoryInterfaceName(configuration, source));
    }

    // visible for testing
    String repositoryClassName(final SqlConfiguration configuration, final Path source) {
        final var repository = userDefinedRepository(configuration)
                .orElseGet(() -> calculateRepositoryName(source));
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_RESULT, repository);
        return repository;
    }

    // visible for testing
    Optional<String> userDefinedRepository(final SqlConfiguration configuration) {
        return configuration.repository()
                .map(this::repositoryClassWithNamePrefix)
                .map(this::repositoryWithNameSuffix)
                .map(this::repositoryInBasePackage);
    }

    // visible for testing
    String calculateRepositoryName(final Path source) {
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_INPUT, files.inputBaseDirectory());
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_SOURCE, source);
        final var relativePathToSqlFile = files.inputBaseDirectory().relativize(source);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RELATIVE, relativePathToSqlFile);
        final var rawRepositoryName = extractRawRepositoryName(relativePathToSqlFile);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RAW, rawRepositoryName);
        final var dottedRepositoryName = dottedRepositoryName(rawRepositoryName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_DOTTED, dottedRepositoryName);
        final var upperCaseName = upperCaseClassName(dottedRepositoryName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_UPPER, upperCaseName);
        final var qualifiedRepository = repositoryInBasePackage(upperCaseName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_QUALIFIED, qualifiedRepository);
        return repositoryClassWithNamePrefix(repositoryWithNameSuffix(qualifiedRepository));
    }

    // visible for testing
    String extractRawRepositoryName(final Path source) {
        return Optional.ofNullable(source.getParent())
                .map(Path::toString)
                .filter(Predicate.not(path -> path.endsWith("..")))
                .orElseGet(() -> repositories.repositoryNamePrefix() + repositories.repositoryNameSuffix());
    }

    // visible for testing
    String dottedRepositoryName(final String rawRepositoryName) {
        return rawRepositoryName.replace(File.separator, ".");
    }

    // visible for testing
    static String upperCaseClassName(final String name) {
        return modifyClassName(name, Strings::upperCase);
    }

    // visible for testing
    String repositoryClassWithNamePrefix(final String repositoryClass) {
        return classWithPrefix(repositoryClass, repositories.repositoryNamePrefix());
    }

    // visible for testing
    String repositoryWithNameSuffix(final String name) {
        return classWithSuffix(name, repositories.repositoryNameSuffix());
    }

    // visible for testing
    String repositoryInBasePackage(final String name) {
        return name.startsWith(repositories.basePackageName())
                ? name
                : repositories.basePackageName() + "." + name;
    }

    private Optional<String> repositoryInterfaceName(final SqlConfiguration configuration, final Path source) {
        if (repositories.generateInterfaces()) {
            final var repositoryClass = userDefinedRepository(configuration)
                    .orElseGet(() -> calculateRepositoryName(source));
            final var repositoryInterface = interfaceName(repositoryClass);
            logger.debug(RepositoryLifecycle.REPOSITORY_NAME_RESULT, repositoryInterface);
            return Optional.of(repositoryInterface);
        }
        return Optional.empty();
    }

    // visible for testing
    String interfaceName(final String repositoryClass) {
        final var rawName = cleanupClassAffixes(repositoryClass);
        final var prefixedName = classWithPrefix(rawName, repositories.repositoryInterfacePrefix());
        final var suffixedName = classWithSuffix(prefixedName, repositories.repositoryInterfaceSuffix());

        return repositoryClass.equals(suffixedName)
                ? modifyClassName(suffixedName, name -> "I" + name)
                : suffixedName;
    }

    private String cleanupClassAffixes(final String repositoryClass) {
        final var withoutPrefix = classWithoutPrefix(repositoryClass, repositories.repositoryNamePrefix());
        return classWithoutSuffix(withoutPrefix, repositories.repositoryNameSuffix());
    }

    private static String classWithPrefix(final String name, final String prefix) {
        return modifyClassName(name, className -> className.startsWith(prefix)
                ? className
                : prefix + className);
    }

    private static String classWithoutPrefix(final String name, final String prefix) {
        return modifyClassName(name, className -> className.startsWith(prefix)
                ? className.replaceFirst(prefix, "")
                : className);
    }

    private static String classWithSuffix(final String name, final String suffix) {
        return modifyClassName(name, className -> className.endsWith(suffix)
                ? className
                : className + suffix);
    }

    private static String classWithoutSuffix(final String name, final String suffix) {
        return modifyClassName(name, className -> className.endsWith(suffix)
                ? Strings.replaceLast(className, suffix, "")
                : className);
    }

    private static String modifyClassName(final String name, final Function<String, String> modifier) {
        if (name.endsWith(".")) {
            return name;
        }
        if (name.contains(".")) {
            final var packageName = name.substring(0, name.lastIndexOf('.') + 1);
            final var className = name.substring(name.lastIndexOf('.') + 1);
            return packageName + modifier.apply(className);
        }
        return modifier.apply(name);
    }

}
