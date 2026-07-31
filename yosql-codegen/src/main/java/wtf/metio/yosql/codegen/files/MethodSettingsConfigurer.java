/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Handles the configuration of method settings, e.g. whether exceptions should be caught and re-thrown.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodSettingsConfigurer {

    /**
     * Configures which settings should be activated for methods.
     *
     * @param configuration The original configuration to adapt.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureSettings(SqlConfiguration configuration);

}
