/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
