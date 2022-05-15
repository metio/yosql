/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
