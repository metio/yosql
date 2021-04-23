/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.gradle;

import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

/**
 * Configures Java related options.
 */
public abstract class Java {

    /**
     * @return The Java SDK API version to use.
     */
    @Input
    public abstract Property<Integer> getApiVersion();

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.usingDefaults()
                .setApiVersion(getApiVersion().get())
                .build();
    }

    void configureConventions() {
        getApiVersion().convention(16);
    }

}
