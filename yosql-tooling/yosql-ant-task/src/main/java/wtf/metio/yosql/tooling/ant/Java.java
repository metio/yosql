/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.ant;

import wtf.metio.yosql.tooling.codegen.model.configuration.JavaConfiguration;

/**
 * Configures Java version and related settings.
 */
public class Java {

    /**
     * The target Java API version (default: <strong>11</strong>).
     */
    private final int apiVersion = 11;

    /**
     * Toggles whether the 'var' keyword should be used (default: <strong>false</strong>).
     */
    private final boolean useVar = false;

    /**
     * Toggles whether records should be used (default: <strong>false</strong>).
     */
    private final boolean useRecords = false;

    /**
     * Toggles whether text blocks should be used (default: <strong>false</strong>).
     */
    private final boolean useTextBlocks = false;

    /**
     * Toggles whether the processing API should be used (default: <strong>true</strong>).
     */
    private final boolean useProcessingApi = true;

    /**
     * Toggles whether the 'final' modified should be used (default: <strong>true</strong>).
     */
    private final boolean useFinal = true;

    /**
     * Toggles whether generics should be used (default: <strong>true</strong>).
     */
    private final boolean useGenerics = true;

    /**
     * Toggles whether the 'diamond' operator should be used (default: <strong>true</strong>).
     */
    private final boolean useDiamondOperator = true;

    /**
     * Toggles whether the 'stream' API should be used (default: <strong>true</strong>).
     */
    private final boolean useStreamAPI = true;

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.builder()
                .setApiVersion(apiVersion)
                .setUseFinal(useFinal)
                .setUseGenerics(useGenerics)
                .setUseDiamondOperator(useDiamondOperator)
                .setUseStreamAPI(useStreamAPI)
                .setUseProcessingApi(useProcessingApi)
                .setUseVar(useVar)
                .setUseTextBlocks(useTextBlocks)
                .setUseRecords(useRecords)
                .build();
    }

}
