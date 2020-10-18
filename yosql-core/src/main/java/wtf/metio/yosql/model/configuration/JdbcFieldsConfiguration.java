/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

/**
 * Configures the various JDBC fields related options.
 */
@Value.Immutable
public interface JdbcFieldsConfiguration {

    static ImmutableJdbcFieldsConfiguration.Builder builder() {
        return ImmutableJdbcFieldsConfiguration.builder();
    }

    /**
     * @return A JDBC fields configuration using default values.
     */
    static ImmutableJdbcFieldsConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The field name suffix for the raw SQL statement.
     */
    @Value.Default
    default String rawSuffix() {
        return "_RAW";
    }

    /**
     * @return The field name suffix for the JDBC parameter indexes.
     */
    @Value.Default
    default String indexSuffix() {
        return "_INDEX";
    }

}
