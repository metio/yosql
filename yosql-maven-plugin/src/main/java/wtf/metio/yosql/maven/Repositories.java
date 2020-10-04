/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.RepositoryConfiguration;

/**
 * Configures how repositories are generated.
 */
public class Repositories {

    /**
     * The repository name suffix to use for all generated repositories (default: <strong>Repository</strong>).
     */
    private final String nameSuffix = "Repository";

    /**
     * Controls whether an interface should be generated for each generated repository. (default:
     * <strong>true</strong>).
     */
    private final boolean generateInterfaces = true;

    /**
     * Controls whether the SQL statements should be inlined in the generated repositories or loaded at options
     * (default: <strong>inline</strong>). Other possible value is <strong>load</strong>.
     */
    private final String repositorySqlStatements = "inline";

    RepositoryConfiguration asConfiguration() {
        return RepositoryConfiguration.builder()
                .setRepositoryGenerateInterface(generateInterfaces)
                .setRepositoryNameSuffix(nameSuffix)
                .build();
    }

}
