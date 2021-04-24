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

    /**
     * @return Should variables and parameters declared as final
     */
    @Input
    public abstract Property<Boolean> getUseFinal();

    /**
     * @return Should generic types be used
     */
    @Input
    public abstract Property<Boolean> getUseGenerics();

    /**
     * @return Should the diamond operator be used
     */
    @Input
    public abstract Property<Boolean> getUseDiamondOperator();

    /**
     * @return Should the stream API be used
     */
    @Input
    public abstract Property<Boolean> getUseStreamAPI();

    /**
     * @return Should variables use the 'var' keyword
     */
    @Input
    public abstract Property<Boolean> getUseVar();

    /**
     * @return Should text blocks be used
     */
    @Input
    public abstract Property<Boolean> getUseTextBlocks();

    /**
     * @return Should records be used
     */
    @Input
    public abstract Property<Boolean> getUseRecords();

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.usingDefaults()
                .setApiVersion(getApiVersion().get())
                .setUseFinal(getUseFinal().get())
                .setUseGenerics(getUseGenerics().get())
                .setUseDiamondOperator(getUseDiamondOperator().get())
                .setUseStreamAPI(getUseStreamAPI().get())
                .setUseVar(getUseVar().get())
                .setUseTextBlocks(getUseTextBlocks().get())
                .setUseRecords(getUseRecords().get())
                .build();
    }

    void configureConventions() {
        getApiVersion().convention(16);
        getUseFinal().convention(true);
        getUseGenerics().convention(true);
        getUseDiamondOperator().convention(true);
        getUseStreamAPI().convention(true);
        getUseVar().convention(true);
        getUseTextBlocks().convention(true);
        getUseRecords().convention(true);
    }

}
