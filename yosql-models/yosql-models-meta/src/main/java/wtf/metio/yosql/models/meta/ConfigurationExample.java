/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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

    static ImmutableConfigurationExample.Builder builder() {
        return ImmutableConfigurationExample.builder();
    }

    static ImmutableConfigurationExample copyOf(final ConfigurationExample example) {
        return ImmutableConfigurationExample.copyOf(example);
    }

    //endregion

    String value();

    String description();

    Optional<String> result();

}
