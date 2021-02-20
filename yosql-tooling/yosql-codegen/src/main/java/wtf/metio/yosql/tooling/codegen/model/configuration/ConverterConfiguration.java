/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.tooling.codegen.model.sql.ResultRowConverter;

import java.util.Set;

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

    /**
     * @return The default converter to use, if no other is specified on a query itself.
     */
    @Value.Default
    default ResultRowConverter defaultConverter() {
        return ResultRowConverter.builder()
                .setAlias("defaultConverter")
                .setConverterType("com.example.persistence.converter.ToResultRowConverter")
                .setMethodName("asUserType")
                .setResultType("com.example.persistence.util.ResultRow")
                .build();
    }

    /**
     * @return The set of known converters. Use this to lookup converters by alias instead of fully-qualified name.
     */
    @Value.Default
    default Set<ResultRowConverter> converters() {
        return Set.of(defaultConverter());
    }

}
