/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.cli.options;

import picocli.CommandLine;
import wtf.metio.yosql.tooling.codegen.model.configuration.RepositoryConfiguration;

/**
 * Configures how repositories are generated.
 */
public class Repositories {

    @CommandLine.Option(
            names = "--repository-suffix",
            description = "The repository name suffix to use for all generated repositories.",
            defaultValue = "Repository")
    String nameSuffix;

    @CommandLine.Option(
            names = "--repository-generate-interfaces",
            description = "Controls whether an interface should be generated for each generated repository.",
            defaultValue = "true")
    boolean generateInterfaces;

    RepositoryConfiguration asConfiguration() {
        return RepositoryConfiguration.builder()
                .setRepositoryGenerateInterface(generateInterfaces)
                .setRepositoryNameSuffix(nameSuffix)
                .build();
    }

}
