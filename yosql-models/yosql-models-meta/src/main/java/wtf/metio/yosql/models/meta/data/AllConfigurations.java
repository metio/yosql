/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import wtf.metio.yosql.models.meta.ConfigurationGroup;

import java.util.stream.Stream;

/**
 * Exposes all configuration groups/settings for convenient access.
 */
public final class AllConfigurations {

    public static Stream<ConfigurationGroup> allConfigurationGroups() {
        return Stream.of(
                Annotations.configurationGroup(),
                Converter.configurationGroup(),
                Files.configurationGroup(),
                Java.configurationGroup(),
                Logging.configurationGroup(),
                Names.configurationGroup(),
                Repositories.configurationGroup(),
                Resources.configurationGroup());
    }

    private AllConfigurations() {
        // factory class
    }

}
