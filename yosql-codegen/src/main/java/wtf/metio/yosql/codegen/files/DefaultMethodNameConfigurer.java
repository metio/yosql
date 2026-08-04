/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.lifecycle.SqlConfigurationLifecycle;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.codegen.exceptions.MissingPrefixConfigurationException;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import javax.lang.model.SourceVersion;
import java.util.Optional;
import java.util.List;

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
        return adapted;
    }

    // visible for testing
    SqlConfiguration baseName(final SqlConfiguration configuration, final String fileName, final int statementInFile) {
        return SqlConfiguration.copyOf(configuration)
                .withName(configuration.name()
                        .filter(DefaultMethodNameConfigurer::isMethodName)
                        .or(() -> configuration.type()
                                .map(type -> validName(type, fileName))
                                .map(name -> calculateName(name, statementInFile)))
                        .or(() -> Optional.of(fileName)
                                .filter(DefaultMethodNameConfigurer::isMethodName)
                                .map(name -> calculateName(name, statementInFile))));
    }

    /**
     * A method has one name rather than a qualified one.
     *
     * <p>{@link SourceVersion#isName(CharSequence)} answers for a <em>type</em> name and so accepts
     * the dots between packages. A file called {@code findTenant.v2.sql} passes it and becomes a
     * method literally called {@code findTenant.v2}, which JavaPoet writes out as readily as it was
     * given — the first complaint comes from the user's compiler.</p>
     */
    private static boolean isMethodName(final String name) {
        return SourceVersion.isIdentifier(name) && !SourceVersion.isKeyword(name);
    }

    private String validName(final SqlStatementType type, final String fileName) {
        return isMethodName(fileName) ? fileName : generateName(type);
    }

    private static String calculateName(final String name, final int statementInFile) {
        return statementInFile > 1 ? name + statementInFile : name;
    }

    private String generateName(final SqlStatementType type) {
        final var prefix = switch (type) {
            case READING -> firstPrefix(repositories.allowedReadPrefixes(), "allowedReadPrefixes");
            case WRITING -> firstPrefix(repositories.allowedWritePrefixes(), "allowedWritePrefixes");
            case CALLING -> firstPrefix(repositories.allowedCallPrefixes(), "allowedCallPrefixes");
        };
        return prefix + "NameWasChanged";
    }

    /**
     * @return the first configured prefix, or a build error naming the setting that was emptied.
     *         An empty list is a misconfiguration, and an {@code IndexOutOfBoundsException} from
     *         inside the generator does not say which setting caused it.
     */
    private static String firstPrefix(final List<String> prefixes, final String setting) {
        if (prefixes.isEmpty()) {
            throw new MissingPrefixConfigurationException(setting);
        }
        return prefixes.getFirst();
    }

}
