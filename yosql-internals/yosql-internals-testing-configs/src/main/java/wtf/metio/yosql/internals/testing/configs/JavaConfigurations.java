/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.internals.testing.configs;

import wtf.metio.yosql.models.immutables.JavaConfiguration;

/**
 * Presets for {@link JavaConfiguration}s.
 */
public final class JavaConfigurations {

    /**
     * @return Preset using the user visible defaults.
     */
    public static JavaConfiguration defaults() {
        return JavaConfiguration.builder().build();
    }

    /**
     * @return Preset for Java 11.
     */
    public static JavaConfiguration java11() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(11)
                .withUseVar(true)
                .withUseTextBlocks(false)
                .withUseSealedInterfaces(false)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
    }

    /**
     * @return Preset for Java 14.
     */
    public static JavaConfiguration java14() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(14)
                .withUseVar(true)
                .withUseTextBlocks(true)
                .withUseSealedInterfaces(false)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
    }

    /**
     * @return Preset for Java 15.
     */
    public static JavaConfiguration java15() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(15)
                .withUseVar(true)
                .withUseTextBlocks(true)
                .withUseSealedInterfaces(true)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
    }

    /**
     * @return Preset for Java 16.
     */
    public static JavaConfiguration java16() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(16)
                .withUseVar(true)
                .withUseTextBlocks(true)
                .withUseSealedInterfaces(true)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
    }

    private JavaConfigurations() {
        // factory class
    }

}

