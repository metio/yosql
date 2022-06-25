/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
     * @return Preset for Java 8.
     */
    @Deprecated
    public static JavaConfiguration java8() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(8)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseSealedInterfaces(false)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
    }

    /**
     * @return Preset for Java 9.
     */
    @Deprecated
    public static JavaConfiguration java9() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(9)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseSealedInterfaces(false)
                .withUseFinalClasses(true)
                .withUseFinalParameters(true)
                .withUseFinalVariables(true);
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

