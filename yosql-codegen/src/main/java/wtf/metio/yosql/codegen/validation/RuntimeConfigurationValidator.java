/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.validation;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

public interface RuntimeConfigurationValidator {

    void validate(RuntimeConfiguration configuration);

}
