/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
