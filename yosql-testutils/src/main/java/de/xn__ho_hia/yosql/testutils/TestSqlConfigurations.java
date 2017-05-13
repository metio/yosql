/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.testutils;

import java.util.Arrays;

import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlParameter;

/**
 * Test mother for {@link SqlConfiguration}s.
 */
@SuppressWarnings("nls")
public final class TestSqlConfigurations {

    private TestSqlConfigurations() {
        // factory class
    }

    /**
     * @return An almost empty configuration with a single parameter called "test".
     */
    public static SqlConfiguration createConfiguration() {
        final SqlConfiguration config = new SqlConfiguration();
        final SqlParameter parameter = new SqlParameter();
        parameter.setName("test");
        config.setParameters(Arrays.asList(parameter));
        return config;
    }

}
