/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.PackagesConfiguration;

/**
 * Configures general packages used during code generation.
 */
public class Packages {

    @CommandLine.Option(
            names = "--package-base-name",
            description = "The base package name for all generated classes.",
            defaultValue = "com.example.persistence")
    String basePackageName;

    @CommandLine.Option(
            names = "--package-utility-name",
            description = "The package name for utility classes.",
            defaultValue = "util")
    String utilityPackageName;

    @CommandLine.Option(
            names = "--package-converter-name",
            description = "The package name for converter classes.",
            defaultValue = "converter")
    String converterPackageName;

    PackagesConfiguration asConfiguration() {
        return PackagesConfiguration.builder()
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                .build();
    }

    String getUtilityPackageName() {
        return basePackageName + "." + utilityPackageName;
    }

}
