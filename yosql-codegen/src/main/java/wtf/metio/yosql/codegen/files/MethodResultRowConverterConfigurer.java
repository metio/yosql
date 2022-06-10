/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
