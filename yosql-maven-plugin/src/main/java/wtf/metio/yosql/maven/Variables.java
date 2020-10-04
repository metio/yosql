/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.VariableConfiguration;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;

/**
 * Configures how variables are generated.
 */
public class Variables {

    @Parameter(defaultValue = "FINAL")
    private String modifiers;
    @Parameter(defaultValue = "TYPE")
    private String variableType;

    VariableConfiguration asConfiguration() {
        return VariableConfiguration.builder()
                .addModifiers(Modifier.valueOf(modifiers))
                .setVariableType(VariableTypeOptions.valueOf(variableType))
                .build();
    }

}
