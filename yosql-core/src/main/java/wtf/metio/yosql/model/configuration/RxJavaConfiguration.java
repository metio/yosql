/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.squareup.javapoet.ClassName;
import org.immutables.value.Value;

/**
 * Configures the various RxJava related options.
 */
@Value.Immutable
public interface RxJavaConfiguration {

    static ImmutableRxJavaConfiguration.Builder builder() {
        return ImmutableRxJavaConfiguration.builder();
    }

    /**
     * @return A RxJava configuration using default values.
     */
    static ImmutableRxJavaConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default ClassName flowStateClass() {
        final var packagesConfiguration = PackagesConfiguration.usingDefaults();
        return ClassName.get(
                packagesConfiguration.basePackageName()
                        + "."
                        + packagesConfiguration.utilityPackageName(),
                "FlowState");
    }

}
