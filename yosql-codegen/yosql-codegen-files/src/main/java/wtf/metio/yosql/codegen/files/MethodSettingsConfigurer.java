/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
