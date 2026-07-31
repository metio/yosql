/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;

/**
 * Handles the configuration of repository names.
 *
 * @see DefaultSqlConfigurationFactory
 */
public interface RepositoryNameConfigurer {

    /**
     * Configures the name of repositories.
     *
     * @param configuration The original configuration to adapt.
     * @param source        The source file where the SQL statement originated from.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureNames(SqlConfiguration configuration, Path source);

}
