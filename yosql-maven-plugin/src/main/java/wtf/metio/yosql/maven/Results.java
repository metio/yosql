/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import com.squareup.javapoet.ClassName;
import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.ResultConfiguration;

/**
 * Configures how results are generated.
 */
public class Results {

    /**
     * The simple name of the generated result row class (default: <strong>ResultRow</strong>).
     */
    @Parameter(required = true, defaultValue = "ResultRow")
    private String resultRow;

    /**
     * The simple name of the generated result state class (default: <strong>ResultState</strong>).
     */
    @Parameter(required = true, defaultValue = "ResultState")
    private String resultState;

    ResultConfiguration asConfiguration(final String utilPackage) {
        return ResultConfiguration.builder()
                .setResultRowClass(ClassName.get(utilPackage, resultRow))
                .setResultStateClass(ClassName.get(utilPackage, resultState))
                .build();
    }

}
