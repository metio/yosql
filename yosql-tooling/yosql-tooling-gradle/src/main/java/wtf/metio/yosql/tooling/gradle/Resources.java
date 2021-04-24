/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.immutables.ResourcesConfiguration;

/**
 * Configures resource usage and other related settings.
 */
public abstract class Resources {

    /**
     * @return The upper limit of threads to use.
     */
    @Input
    public abstract Property<Integer> getMaxThreads();

    ResourcesConfiguration asConfiguration() {
        return ResourcesConfiguration.usingDefaults()
                .setMaxThreads(getMaxThreads().get())
                .build();
    }

    void configureConventions() {
        getMaxThreads().convention(1);
    }

}
