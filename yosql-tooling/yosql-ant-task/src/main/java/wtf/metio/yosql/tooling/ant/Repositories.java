/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.ant;

import wtf.metio.yosql.tooling.codegen.model.configuration.RepositoryConfiguration;

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
     * The base package name for all repositories (default: <strong>com.example.persistence</strong>).
     */
    private final String basePackageName = "com.example.persistence";

    /**
     * Controls whether RxJava compatible methods should be generated. (default:
     * <strong>true</strong>).
     */
    private final boolean generateRxJavaApi = false;

    RepositoryConfiguration asConfiguration() {
        return RepositoryConfiguration.builder()
                .setRepositoryGenerateInterface(generateInterfaces)
                .setRepositoryNameSuffix(nameSuffix)
                .setBasePackageName(basePackageName)
                .setGenerateRxJavaApi(generateRxJavaApi)
                .build();
    }

}
