/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.validation;

import ch.qos.cal10n.IMessageConveyor;
import wtf.metio.yosql.codegen.exceptions.InvalidConverterConfigurationException;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.util.Map;
import java.util.stream.Collectors;

import static wtf.metio.yosql.codegen.lifecycle.ApplicationErrors.NAMES_CONFIG_INVALID;

/**
 * Validates {@link wtf.metio.yosql.models.immutables.ConverterConfiguration}s.
 */
public final class ConverterConfigurationValidator implements RuntimeConfigurationValidator {

    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    public ConverterConfigurationValidator(final ExecutionErrors errors, final IMessageConveyor messages) {
        this.errors = errors;
        this.messages = messages;
    }

    @Override
    public void validate(final RuntimeConfiguration configuration) {
        final var converter = configuration.converter();

        final var counts = converter.uniqueConverterAliases();
        final var duplicates = counts.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (!duplicates.isEmpty()) {
            errors.add(new InvalidConverterConfigurationException(messages.getMessage(NAMES_CONFIG_INVALID, duplicates)));
        }
    }

}
