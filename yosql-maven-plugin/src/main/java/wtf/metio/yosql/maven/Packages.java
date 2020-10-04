/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.PackagesConfiguration;

/**
 * Configures general packages used during code generation.
 */
public class Packages {

    /**
     * The base package name for all generated classes (default: <strong>com.example.persistence</strong>).
     */
    private final String basePackageName = "com.example.persistence";

    /**
     * The package name for utility classes (default: <strong>util</strong>).
     */
    private final String utilityPackageName = "util";

    /**
     * The package name for utility classes (default: <strong>converter</strong>).
     */
    private final String converterPackageName = "converter";

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
