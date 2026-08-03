/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DefaultRepositoryNameConfigurer implements RepositoryNameConfigurer {

    /**
     * What a repository is called after the directory its statements sit in. Fixed, because the name
     * only has to tell a reader that {@code TenantRepository} is where the tenant statements went —
     * and an interface generated alongside it takes the same name without the suffix, which is what
     * keeps the two apart.
     */
    private static final String NAME_SUFFIX = "Repository";

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
        return repositoryWithNameSuffix(qualifiedRepository);
    }

    // visible for testing
    String extractRawRepositoryName(final Path source) {
        return Optional.ofNullable(source.getParent())
                .map(Path::toString)
                .filter(Predicate.not(path -> path.endsWith("..")))
                .orElseGet(() -> NAME_SUFFIX);
    }

    /**
     * Joins a relative path's segments with dots.
     *
     * <p>Asking the path for its segments rather than splitting its string on {@link File#separator}:
     * the separator is the running platform's, and a path from another {@link java.nio.file.FileSystem}
     * — a zip, a test double — uses its own.</p>
     */
    // visible for testing
    String dottedRepositoryName(final String rawRepositoryName) {
        return rawRepositoryName.replace(File.separatorChar, '.').replace('/', '.');
    }

    // visible for testing
    static String upperCaseClassName(final String name) {
        return modifyClassName(name, Strings::upperCase);
    }

    // visible for testing
    String repositoryWithNameSuffix(final String name) {
        return classWithSuffix(name, NAME_SUFFIX);
    }

    // visible for testing
    String repositoryInBasePackage(final String repositoryName) {
        final var basePackages = repositories.basePackageName().split("\\.");
        final var repositoryPackages = repositoryName.split("\\.");
        final var iterationLength = Math.min(basePackages.length, repositoryPackages.length);

        int overlappingPackages = 0;
        for (int index = 0; index < iterationLength - 1; index++) {
            if (basePackages[basePackages.length - 1 - index].equals(repositoryPackages[index])) {
                overlappingPackages++;
            }
        }

        return Stream.concat(Arrays.stream(basePackages), Arrays.stream(repositoryPackages)
                        .skip(overlappingPackages))
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining("."));
    }

    private Optional<String> repositoryInterfaceName(final SqlConfiguration configuration, final Path source) {
        if (repositories.generateInterfaces()) {
            final var repositoryClass = userDefinedRepository(configuration)
                    .orElseGet(() -> calculateRepositoryName(source));
            final var repositoryInterface = interfaceName(repositoryClass);
            logger.debug(RepositoryLifecycle.REPOSITORY_INTERFACE_RESULT, repositoryInterface);
            return Optional.of(repositoryInterface);
        }
        return Optional.empty();
    }

    // visible for testing
    String interfaceName(final String repositoryClass) {
        logger.debug(RepositoryLifecycle.REPOSITORY_INTERFACE_CALC_SOURCE, repositoryClass);
        final var rawName = classWithoutSuffix(repositoryClass, NAME_SUFFIX);
        logger.debug(RepositoryLifecycle.REPOSITORY_INTERFACE_CALC_RAW, rawName);

        // A repository the author named themselves may not end in the suffix, and then the interface
        // would be the class. Prefixing is what keeps the two names apart in that one case.
        return repositoryClass.equals(rawName)
                ? modifyClassName(rawName, name -> "I" + name)
                : rawName;
    }

    private static String classWithSuffix(final String name, final String suffix) {
        return modifyClassName(name, className -> className.endsWith(suffix)
                ? className
                : className + suffix);
    }

    /**
     * Drops the suffix, unless the suffix is the whole name. A repository somebody called
     * {@code Repository} would otherwise leave a package and no class behind.
     */
    private static String classWithoutSuffix(final String name, final String suffix) {
        return modifyClassName(name, className -> className.endsWith(suffix) && !className.equals(suffix)
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
