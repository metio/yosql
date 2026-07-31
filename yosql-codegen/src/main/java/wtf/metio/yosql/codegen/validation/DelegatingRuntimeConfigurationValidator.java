/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.validation;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.util.Set;

/**
 * Delegates {@link RuntimeConfiguration} validation to a set of {@link RuntimeConfigurationValidator}s.
 */
public final class DelegatingRuntimeConfigurationValidator implements RuntimeConfigurationValidator {

    private final Set<RuntimeConfigurationValidator> validators;

    public DelegatingRuntimeConfigurationValidator(final Set<RuntimeConfigurationValidator> validators) {
        this.validators = validators;
    }

    @Override
    public void validate(final RuntimeConfiguration configuration) {
        validators.forEach(validator -> validator.validate(configuration));
    }

}
