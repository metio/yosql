/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
        if (repositories.validateMethodNamePrefixes()) {
            configuration.type()
                    .map(this::allowedPrefixes)
                    .flatMap(prefixes -> configuration.name()
                            .filter(name -> notStartsWith(name, prefixes)))
                    .ifPresent(name -> invalidPrefix(source, name));
        }
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
