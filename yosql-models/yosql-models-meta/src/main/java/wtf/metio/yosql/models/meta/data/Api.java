/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;

public final class Api {

    public static ConfigurationGroup configurationGroup() {
        return ConfigurationGroup.builder()
                .setName(Api.class.getSimpleName())
                .setDescription("Configures which external APIs should be used.")
                .addSettings(daoApi())
                .addSettings(loggingApi())
                .build();
    }

    private static ConfigurationSetting daoApi() {
        return ConfigurationSetting.builder()
                .setName("daoApi")
                .setDescription("The DAO API to use (default: <strong>JDBC</strong>).")
                .setType(TypeName.get(PersistenceApis.class))
                .setValue(PersistenceApis.JDBC)
                .build();
    }

    private static ConfigurationSetting loggingApi() {
        return ConfigurationSetting.builder()
                .setName("loggingApi")
                .setDescription("The logging API to use (default: <strong>NONE</strong>).")
                .setType(TypeName.get(LoggingApis.class))
                .setValue(LoggingApis.NONE)
                .build();
    }

    private Api() {
        // data class
    }

}
