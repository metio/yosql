/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.ResourceConfiguration;

/**
 * Configures resources used during code generation.
 */
public class Resources {

    @CommandLine.Option(
            names = "--resources-max-threads",
            description = "The maximum number of threads to use during code generation.",
            defaultValue = "1")
    int maxThreads;

    ResourceConfiguration asConfiguration() {
        return ResourceConfiguration.builder()
                .setMaxThreads(maxThreads)
                .build();
    }

}
