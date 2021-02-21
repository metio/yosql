/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.model.configuration;

import org.immutables.value.Value;

/**
 * Configures the various Java language related options.
 */
@Value.Immutable
@Deprecated(forRemoval = true) // Use class "Java" instead
public interface JavaConfiguration {

    static ImmutableJavaConfiguration.Builder builder() {
        return ImmutableJavaConfiguration.builder();
    }

    /**
     * @return A Java configuration using default values.
     */
    static ImmutableJavaConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The Java SDK API version to use.
     */
    @Value.Default
    default int apiVersion() {
        return 15;
    }

    /**
     * @return Should variables and parameters declared as final
     * @since Java 1
     */
    @Value.Default
    default boolean useFinal() {
        return true;
    }

    /**
     * @return Should generic types be used
     * @since Java 5
     */
    @Value.Default
    default boolean useGenerics() {
        return true;
    }

    /**
     * @return Should the diamond operator be used
     * @since Java 7
     */
    @Value.Default
    default boolean useDiamondOperator() {
        return true;
    }

    /**
     * @return Should the stream API be used
     * @since Java 8
     */
    @Value.Default
    default boolean useStreamAPI() {
        return true;
    }

    /**
     * @return Should the processing API be used
     * @since Java 9
     */
    @Value.Default
    default boolean useProcessingApi() {
        return true;
    }

    /**
     * @return Should variables use the 'var' keyword
     * @since Java 11
     */
    @Value.Default
    default boolean useVar() {
        return false;
    }

    /**
     * @return Should text blocks be used
     * @since Java 15
     */
    @Value.Default
    default boolean useTextBlocks() {
        return false;
    }

    /**
     * @return Should records be used
     * @since Java 16?
     */
    @Value.Default
    default boolean useRecords() {
        return false;
    }

}
