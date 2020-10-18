/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

/**
 * Configuration for the various package related options.
 */
@Value.Immutable
public interface PackagesConfiguration {

    static ImmutablePackagesConfiguration.Builder builder() {
        return ImmutablePackagesConfiguration.builder();
    }

    /**
     * @return An package configuration using default values.
     */
    static ImmutablePackagesConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The package name for utilities.
     */
    @Value.Default
    default String utilityPackageName() {
        return "util";
    }

    /**
     * @return The package name for converters.
     */
    @Value.Default
    default String converterPackageName() {
        return "converter";
    }

    /**
     * @return The base package name for all classes.
     */
    @Value.Default
    default String basePackageName() {
        return "com.example.persistence";
    }

}
