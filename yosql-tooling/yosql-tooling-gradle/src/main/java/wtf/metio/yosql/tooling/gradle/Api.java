/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;

/**
 * Configures which external APIs should be used.
 */
public abstract class Api {

    /**
     * @return The DAO API to use (default: <strong>JDBC</strong>).
     */
    @Input
    public abstract Property<PersistenceApis> getDaoApi();

    /**
     * @return The logging API to use (default: <strong>NONE</strong>).
     */
    @Input
    public abstract Property<LoggingApis> getLoggingApi();

    ApiConfiguration asConfiguration() {
        return ApiConfiguration.usingDefaults()
                .setDaoApi(getDaoApi().get())
                .setLoggingApi(getLoggingApi().get())
                .build();
    }

    void configureConventions() {
        getDaoApi().convention(PersistenceApis.JDBC);
        getLoggingApi().convention(LoggingApis.NONE);
    }

}
