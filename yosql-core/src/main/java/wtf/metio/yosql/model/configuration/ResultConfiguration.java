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
 * Configures the various result related options.
 */
@Value.Immutable
public interface ResultConfiguration {

    static ImmutableResultConfiguration.Builder builder() {
        return ImmutableResultConfiguration.builder();
    }

    /**
     * @return A result configuration using default values.
     */
    static ImmutableResultConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default ClassName resultStateClass() {
        final var packagesConfiguration = PackagesConfiguration.usingDefaults();
        return ClassName.get(
                packagesConfiguration.basePackageName()
                        + "."
                        + packagesConfiguration.utilityPackageName(),
                "ResultState");
    }

    @Value.Default
    default ClassName resultRowClass() {
        final var packagesConfiguration = PackagesConfiguration.usingDefaults();
        return ClassName.get(
                packagesConfiguration.basePackageName()
                        + "."
                        + packagesConfiguration.utilityPackageName(),
                "ResultRow");
    }

}
