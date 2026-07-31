/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.validation;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

public final class DefaultRuntimeValidator implements RuntimeValidator {

    private final RuntimeConfiguration runtimeConfiguration;
    private final RuntimeConfigurationValidator runtimeConfigurationValidator;

    public DefaultRuntimeValidator(
            final RuntimeConfiguration runtimeConfiguration,
            final RuntimeConfigurationValidator runtimeConfigurationValidator) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.runtimeConfigurationValidator = runtimeConfigurationValidator;
    }

    @Override
    public void validate() {
        runtimeConfigurationValidator.validate(runtimeConfiguration);
    }

}
