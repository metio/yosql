/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.lifecycle.ExecutionErrors;
import wtf.metio.yosql.codegen.lifecycle.ValidationErrors;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.List;

import static wtf.metio.yosql.models.configuration.SqlType.*;

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
            switch (configuration.type()) {
                case READING:
                    if (notStartsWith(configuration.name(), repositories.allowedReadPrefixes())) {
                        invalidPrefix(source, READING, configuration.name());
                    }
                    break;
                case WRITING:
                    if (notStartsWith(configuration.name(), repositories.allowedWritePrefixes())) {
                        invalidPrefix(source, WRITING, configuration.name());
                    }
                    break;
                case CALLING:
                    if (notStartsWith(configuration.name(), repositories.allowedCallPrefixes())) {
                        invalidPrefix(source, CALLING, configuration.name());
                    }
                    break;
                default:
                    errors.illegalArgument(messages.getMessage(ValidationErrors.UNSUPPORTED_TYPE, source, configuration.type()));
                    break;
            }
        }
    }

    private static boolean notStartsWith(final String fileName, final List<String> prefixes) {
        return prefixes == null || prefixes.stream().noneMatch(fileName::startsWith);
    }

    private void invalidPrefix(final Path source, final SqlType sqlType, final String name) {
        errors.illegalArgument(messages.getMessage(ValidationErrors.INVALID_PREFIX, source, sqlType, name));
    }

}
