/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.tooling.codegen.model.configuration.JavaConfiguration;

/**
 * Configures Java version and related settings.
 */
public class Java {

    @CommandLine.Option(
            names = "--java-api-version",
            description = "The target Java API version to use.",
            defaultValue = "11")
    int apiVersion;

    @CommandLine.Option(
            names = "--java-use-var",
            description = "Whether the 'var' keyword should be used.",
            defaultValue = "false")
    boolean useVar;

    @CommandLine.Option(
            names = "--java-use-records",
            description = "Whether the records should be used.",
            defaultValue = "false")
    boolean useRecords;

    @CommandLine.Option(
            names = "--java-use-text-blocks",
            description = "Whether the text blocks should be used.",
            defaultValue = "false")
    boolean useTextBlocks;

    @CommandLine.Option(
            names = "--java-use-processing-api",
            description = "Whether the processing API should be used.",
            defaultValue = "true")
    boolean useProcessingApi;

    @CommandLine.Option(
            names = "--java-use-final",
            description = "Whether the 'final' keyword should be used.",
            defaultValue = "true")
    boolean useFinal;

    @CommandLine.Option(
            names = "--java-use-generics",
            description = "Whether generics should be used.",
            defaultValue = "true")
    boolean useGenerics;

    @CommandLine.Option(
            names = "--java-use-diamond-operator",
            description = "Whether the diamond operator should be used.",
            defaultValue = "true")
    boolean useDiamondOperator;

    @CommandLine.Option(
            names = "--java-use-stream-api",
            description = "Whether the stream API should be used.",
            defaultValue = "true")
    boolean useStreamApi;

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.builder()
                .setApiVersion(apiVersion)
                .setUseFinal(useFinal)
                .setUseGenerics(useGenerics)
                .setUseDiamondOperator(useDiamondOperator)
                .setUseStreamAPI(useStreamApi)
                .setUseProcessingApi(useProcessingApi)
                .setUseVar(useVar)
                .setUseTextBlocks(useTextBlocks)
                .setUseRecords(useRecords)
                .build();
    }

}
