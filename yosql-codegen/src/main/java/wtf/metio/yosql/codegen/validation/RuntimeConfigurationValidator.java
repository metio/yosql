/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.validation;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

public interface RuntimeConfigurationValidator {

    void validate(RuntimeConfiguration configuration);

}
