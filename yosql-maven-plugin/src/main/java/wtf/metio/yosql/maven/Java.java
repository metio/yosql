/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.JavaConfiguration;

/**
 * Configures Java version and related settings.
 */
public class Java {

    /**
     * The target Java source version (default: <strong>11</strong>).
     */
    @Parameter(defaultValue = "11")
    private String targetVersion;

    @Parameter(defaultValue = "false")
    private boolean useVar;

    @Parameter(defaultValue = "false")
    private boolean useRecords;

    @Parameter(defaultValue = "false")
    private boolean useTextBlocks;

    @Parameter(defaultValue = "true")
    private boolean useProcessingApi;

    @Parameter(defaultValue = "false")
    private boolean useFinal;

    JavaConfiguration asConfiguration() {
        final var javaVersion = Integer.parseInt(this.targetVersion);
        return JavaConfiguration.builder()
                .setTargetVersion(javaVersion)
                .setUseFinal(useFinal)
                .setUseGenerics(javaVersion >= 5)
                .setUseDiamondOperator(javaVersion >= 7)
                .setUseStreamAPI(javaVersion >= 8)
                .setUseProcessingApi(useProcessingApi)
                .setUseVar(useVar)
                .setUseTextBlocks(useTextBlocks)
                .setUseRecords(useRecords)
                .build();
    }

}
