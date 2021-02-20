/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.ant;

import wtf.metio.yosql.tooling.codegen.model.configuration.ResourceConfiguration;

/**
 * Configures resources used during code generation.
 */
public class Resources {

    /**
     * The maximum number of threads to use during code generation.
     */
    private final int maxThreads = 1;

    ResourceConfiguration asConfiguration() {
        return ResourceConfiguration.builder()
                .setMaxThreads(maxThreads)
                .build();
    }

}
