/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.lifecycle.ValidationErrors;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.List;

public final class DefaultMethodNameValidator implements MethodNameValidator {

    private final RepositoriesConfiguration repositories;
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    public DefaultMethodNameValidator(
            final RepositoriesConfiguration repositories,
            final ExecutionErrors errors,
            final IMessageConveyor messages) {
        this.repositories = repositories;
        this.errors = errors;
        this.messages = messages;
    }

    @Override
    public void validateNames(final SqlConfiguration configuration, final Path source) {
        // Not gated on validateMethodNamePrefixes: that option is about naming style, while a
        // statement of unknown kind generates no method at all. A build that succeeds and produces
        // nothing is the worst outcome available, so this one always fails.
        if (configuration.type().isEmpty()) {
            unknownType(source, configuration.name().orElse(""));
            return;
        }
        if (repositories.validateMethodNamePrefixes()) {
            configuration.type()
                    .map(this::allowedPrefixes)
                    .flatMap(prefixes -> configuration.name()
                            .filter(name -> notStartsWith(name, prefixes)))
                    .ifPresent(name -> invalidPrefix(source, name));
        }
    }

    private void unknownType(final Path source, final String name) {
        errors.illegalArgument(messages.getMessage(ValidationErrors.UNKNOWN_STATEMENT_TYPE,
                source, name,
                repositories.allowedReadPrefixes(),
                repositories.allowedWritePrefixes(),
                repositories.allowedCallPrefixes()));
    }

    private List<String> allowedPrefixes(final SqlStatementType type) {
        return switch (type) {
            case READING -> repositories.allowedReadPrefixes();
            case WRITING -> repositories.allowedWritePrefixes();
            case CALLING -> repositories.allowedCallPrefixes();
        };
    }

    private static boolean notStartsWith(final String fileName, final List<String> prefixes) {
        return prefixes == null || prefixes.stream().noneMatch(fileName::startsWith);
    }

    private void invalidPrefix(final Path source, final String name) {
        errors.illegalArgument(messages.getMessage(ValidationErrors.INVALID_PREFIX, source, name));
    }

}
