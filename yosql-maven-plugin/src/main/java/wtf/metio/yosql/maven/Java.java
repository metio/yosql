/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.JavaConfiguration;

/**
 * Configures Java version and related settings.
 */
public class Java {

    /**
     * The target Java API version (default: <strong>11</strong>).
     */
    private final int apiVersion = 11;

    private final boolean useVar = false;

    private final boolean useRecords = false;

    private final boolean useTextBlocks = false;

    private final boolean useProcessingApi = true;

    private final boolean useFinal = true;

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.builder()
                .setApiVersion(apiVersion)
                .setUseFinal(useFinal)
                .setUseGenerics(apiVersion >= 5)
                .setUseDiamondOperator(apiVersion >= 7)
                .setUseStreamAPI(apiVersion >= 8)
                .setUseProcessingApi(useProcessingApi)
                .setUseVar(useVar)
                .setUseTextBlocks(useTextBlocks)
                .setUseRecords(useRecords)
                .build();
    }

}
