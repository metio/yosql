/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Handles the configuration of method names, e.g. the batch method prefix and suffix.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodNameConfigurer {

    /**
     * Configures method names and their affixes.
     *
     * @param configuration   The original configuration to adapt.
     * @param fileName        The file name where the SQL statement originated from.
     * @param statementInFile Which number of statement the given configuration represents in the source file.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureNames(SqlConfiguration configuration, String fileName, int statementInFile);

}
