/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;

/**
 * Configures repository related options.
 */
public abstract class Repositories {

    /**
     * @return The base package name for all repositories
     */
    @Input
    public abstract Property<String> getBasePackageName();

    /**
     * @return The repository name suffix to use.
     */
    @Input
    public abstract Property<String> getRepositoryNameSuffix();

    RepositoriesConfiguration asConfiguration() {
        return RepositoriesConfiguration.usingDefaults()
                .setBasePackageName(getBasePackageName().get())
                .setRepositoryNameSuffix(getRepositoryNameSuffix().get())
                .build();
    }

    void configureConventions() {
        getBasePackageName().convention("com.example.persistence");
        getRepositoryNameSuffix().convention("Repository");
    }

}
