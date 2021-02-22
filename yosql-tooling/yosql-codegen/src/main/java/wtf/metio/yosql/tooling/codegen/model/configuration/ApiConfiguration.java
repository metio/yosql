/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.tooling.codegen.options.DaoApiOptions;
import wtf.metio.yosql.tooling.codegen.options.LoggingApiOptions;

/**
 * Configures the various API related options.
 */
@Value.Immutable
public interface ApiConfiguration {

    static ImmutableApiConfiguration.Builder builder() {
        return ImmutableApiConfiguration.builder();
    }

    /**
     * @return A logging configuration using default values.
     */
    static ImmutableApiConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The logging API to use.
     */
    @Value.Default
    default LoggingApiOptions loggingApi() {
        return LoggingApiOptions.JDK;
    }

    /**
     * @return The DAO API to use.
     */
    @Value.Default
    default DaoApiOptions daoApi() {
        return DaoApiOptions.JDBC;
    }

}
