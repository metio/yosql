/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Represents an example for a single configuration setting with a value
 */
@Value.Immutable
public interface ConfigurationExample {

    //region builders

    static ImmutableConfigurationExample.ValueBuildStage builder() {
        return ImmutableConfigurationExample.builder();
    }

    static ImmutableConfigurationExample copyOf(final ConfigurationExample example) {
        return ImmutableConfigurationExample.copyOf(example);
    }

    //endregion

    /**
     * @return The configuration value this example is written for.
     */
    String value();

    /**
     * @return The description of this example.
     */
    String description();

    /**
     * @return The resulting code snippet of this example.
     */
    Optional<String> result();

}
