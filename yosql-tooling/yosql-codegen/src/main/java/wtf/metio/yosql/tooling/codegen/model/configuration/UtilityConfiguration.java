/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import com.squareup.javapoet.ClassName;
import org.immutables.value.Value;

/**
 * Configuration for the various utility related options.
 */
@Value.Immutable
public interface UtilityConfiguration {

    static ImmutableUtilityConfiguration.Builder builder() {
        return ImmutableUtilityConfiguration.builder();
    }

    /**
     * @return An package configuration using default values.
     */
    static ImmutableUtilityConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The base package name for all converter related classes.
     */
    @Value.Default
    default String basePackageName() {
        return "com.example.persistence.util";
    }

    @Value.Default
    default ClassName resultStateClass() {
        return ClassName.get(basePackageName(), "ResultState");
    }

    @Value.Default
    default ClassName resultRowClass() {
        return ClassName.get(basePackageName(), "ResultRow");
    }

    @Value.Default
    default ClassName flowStateClass() {
        return ClassName.get(basePackageName(), "FlowState");
    }

}
