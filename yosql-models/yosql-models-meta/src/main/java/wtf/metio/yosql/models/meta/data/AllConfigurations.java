/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.models.meta.data;

import wtf.metio.yosql.models.meta.ConfigurationGroup;

import java.util.List;

/**
 * Exposes all configuration groups/settings for convenient access.
 */
public final class AllConfigurations {

    public static List<ConfigurationGroup> allConfigurationGroups() {
        return List.of(
                Annotations.configurationGroup(),
                Api.configurationGroup(),
                Files.configurationGroup(),
                Repositories.configurationGroup(),
                Java.configurationGroup(),
                Jdbc.configurationGroup(),
                Resources.configurationGroup());
    }

}
