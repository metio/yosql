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
     * The target Java source version (default: <strong>1.8</strong>). Use either '1.X' syntax or just '8', '11'
     * and so on.
     */
    @Parameter(required = true, defaultValue = "1.8")
    private String java;

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.builder()
                .setTargetVersion(1)
                .setUseVar(true) // TODO: configure w/ Maven
                .setUseRecords(true) // TODO: configure w/ Maven
                .build();
    }

}
