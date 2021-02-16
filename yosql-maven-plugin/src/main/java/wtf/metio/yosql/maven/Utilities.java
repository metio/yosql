/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import com.squareup.javapoet.ClassName;
import wtf.metio.yosql.model.configuration.UtilityConfiguration;

/**
 * Configures general utilities used during code generation.
 */
public class Utilities {

    /**
     * The base package name for all generated utility classes (default: <strong>com.example.persistence.util</strong>).
     */
    private final String basePackageName = "com.example.persistence.util";

    /**
     * The simple name of the generated result row class (default: <strong>ResultRow</strong>).
     */
    private final String resultRow = "ResultRow";

    /**
     * The simple name of the generated result state class (default: <strong>ResultState</strong>).
     */
    private final String resultState = "ResultState";

    /**
     * The simple name of the generated flow state class (default: <strong>FlowState</strong>).
     */
    private final String flowState = "FlowState";

    UtilityConfiguration asConfiguration() {
        return UtilityConfiguration.builder()
                .setBasePackageName(basePackageName)
                .setResultRowClass(ClassName.get(basePackageName, resultRow))
                .setResultStateClass(ClassName.get(basePackageName, resultState))
                .setFlowStateClass(ClassName.get(basePackageName, flowState))
                .build();
    }

}
