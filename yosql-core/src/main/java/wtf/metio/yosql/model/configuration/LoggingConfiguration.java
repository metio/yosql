/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.model.options.LoggingApiOptions;

/**
 * Configures the various logging related options.
 */
@Value.Immutable
public interface LoggingConfiguration {

    static ImmutableLoggingConfiguration.Builder builder() {
        return ImmutableLoggingConfiguration.builder();
    }

    /**
     * @return A logging configuration using default values.
     */
    static ImmutableLoggingConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return Whether log statements should be generated
     */
    default boolean shouldLog() {
        return LoggingApiOptions.NONE != api();
    }

    /**
     * @return The logging API to use.
     */
    @Value.Default
    default LoggingApiOptions api() {
        return LoggingApiOptions.JDK;
    }

}
