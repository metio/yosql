/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

import java.util.List;

/**
 * Configures the various methods related options.
 */
@Value.Immutable
public interface MethodConfiguration {

    static ImmutableMethodConfiguration.Builder builder() {
        return ImmutableMethodConfiguration.builder();
    }

    /**
     * @return A method configuration using default values.
     */
    static ImmutableMethodConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default boolean generateStandardApi() {
        return true;
    }

    @Value.Default
    default boolean generateBatchApi() {
        return false;
    }

    @Value.Default
    default boolean generateRxJavaApi() {
        return false;
    }

    @Value.Default
    default boolean generateStreamEagerApi() {
        return false;
    }

    @Value.Default
    default boolean generateStreamLazyApi() {
        return false;
    }

    @Value.Default
    default boolean methodCatchAndRethrow() {
        return true;
    }

    @Value.Default
    default boolean validateMethodNamePrefixes() {
        return true;
    }

    @Value.Default
    default String methodBatchPrefix() {
        return "";
    }

    @Value.Default
    default String methodBatchSuffix() {
        return "Batch";
    }

    @Value.Default
    default String methodStreamPrefix() {
        return "";
    }

    @Value.Default
    default String methodStreamSuffix() {
        return "Stream";
    }

    @Value.Default
    default String methodRxJavaPrefix() {
        return "";
    }

    @Value.Default
    default String methodRxJavaSuffix() {
        return "Flow";
    }

    @Value.Default
    default String methodLazyName() {
        return "Lazy";
    }

    @Value.Default
    default String methodEagerName() {
        return "Eager";
    }

    @Value.Default
    default List<String> allowedWritePrefixes() {
        return List.of("update", "insert", "delete", "create", "write", "add", "remove", "merge", "drop");
    }

    @Value.Default
    default List<String> allowedReadPrefixes() {
        return List.of("select", "read", "query", "find");
    }

    @Value.Default
    default List<String> allowedCallPrefixes() {
        return List.of("call", "execute");
    }

}
