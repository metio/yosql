/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Handles the configuration of method APIs, e.g. whether batch methods should be generated.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodApiConfigurer {

    /**
     * Configures which APIs should be generated for the given configuration.
     *
     * @param configuration The original configuration to adapt.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureApis(SqlConfiguration configuration);

}
