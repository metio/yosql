/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;

/**
 * Configures the various resource related options.
 */
@Value.Immutable
public interface ResourceConfiguration {

    static ImmutableResourceConfiguration.Builder builder() {
        return ImmutableResourceConfiguration.builder();
    }

    /**
     * @return A resource configuration using default values.
     */
    static ImmutableResourceConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The maximum number of threads to use while working.
     */
    @Value.Default
    default int maxThreads() {
        return 0;
    }

}
