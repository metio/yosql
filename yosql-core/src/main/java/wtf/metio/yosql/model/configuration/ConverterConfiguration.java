/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

/**
 * Configuration for the various converter related options.
 */
@Value.Immutable
public interface ConverterConfiguration {

    static ImmutableConverterConfiguration.Builder builder() {
        return ImmutableConverterConfiguration.builder();
    }

    /**
     * @return An converter configuration using default values.
     */
    static ImmutableConverterConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The base package name for all converter related classes.
     */
    @Value.Default
    default String basePackageName() {
        return "com.example.persistence.converter";
    }
}
