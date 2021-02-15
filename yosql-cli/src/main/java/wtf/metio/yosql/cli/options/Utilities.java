/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.UtilityConfiguration;

/**
 * Configures general utilities used during code generation.
 */
public class Utilities {

    @CommandLine.Option(
            names = "--package-base-name",
            description = "The base package name for all generated classes.",
            defaultValue = "com.example.persistence.util")
    String basePackageName;

    public UtilityConfiguration asConfiguration() {
        return UtilityConfiguration.builder()
                .setBasePackageName(basePackageName)
                .build();
    }

    public String getUtilityPackageName() {
        return basePackageName;
    }

}
