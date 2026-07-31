/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Handles the configuration of method parameters, e.g. which type and index they have.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodParameterConfigurer {

    /**
     * Configures method parameters.
     *
     * @param configuration The original configuration to adapt.
     * @return An adapted version of the original.
     */
    SqlConfiguration configureParameters(SqlConfiguration configuration, Path source, Map<String, List<Integer>> parameterIndices);

}
