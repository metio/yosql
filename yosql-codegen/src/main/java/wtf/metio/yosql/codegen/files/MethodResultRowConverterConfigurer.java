/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Handles the configuration of method converters, e.g. which type to use.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodResultRowConverterConfigurer {

    /**
     * Configures method converters.
     *
     * @param configuration The original configuration to adapt.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureResultRowConverter(SqlConfiguration configuration);

}
