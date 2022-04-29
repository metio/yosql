/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.immutables.JavaConfiguration;

public final class JavaConfigurations {

    public static JavaConfiguration defaults() {
        return JavaConfiguration.usingDefaults().build();
    }

    public static JavaConfiguration java4() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(4)
                .withUseDiamondOperator(false)
                .withUseGenerics(false)
                .withUseRecords(false)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseStreamAPI(false)
                .withUseFinal(true);
    }

    public static JavaConfiguration java5() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(5)
                .withUseDiamondOperator(false)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseStreamAPI(false)
                .withUseFinal(true);
    }

    public static JavaConfiguration java7() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(7)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseStreamAPI(false)
                .withUseFinal(true);
    }

    public static JavaConfiguration java8() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(8)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseStreamAPI(true)
                .withUseFinal(true);
    }

    /**
     * @return Preset for JavaConfigurations 9.
     */
    public static JavaConfiguration java9() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(9)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(false)
                .withUseTextBlocks(false)
                .withUseStreamAPI(true)
                .withUseFinal(true);
    }

    /**
     * @return Preset for JavaConfigurations 11.
     */
    public static JavaConfiguration java11() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(11)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(true)
                .withUseTextBlocks(false)
                .withUseStreamAPI(true)
                .withUseFinal(true);
    }

    public static JavaConfiguration java14() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(14)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(false)
                .withUseVar(true)
                .withUseTextBlocks(true)
                .withUseStreamAPI(true)
                .withUseFinal(true);
    }

    public static JavaConfiguration java16() {
        return JavaConfiguration.copyOf(defaults())
                .withApiVersion(16)
                .withUseDiamondOperator(true)
                .withUseGenerics(true)
                .withUseRecords(true)
                .withUseVar(true)
                .withUseTextBlocks(true)
                .withUseStreamAPI(true)
                .withUseFinal(true);
    }

    private JavaConfigurations() {
        // factory class
    }

}

