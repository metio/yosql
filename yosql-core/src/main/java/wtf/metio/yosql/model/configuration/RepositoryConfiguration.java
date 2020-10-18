/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;

/**
 * Configures the various repository related options.
 */
@Value.Immutable
public interface RepositoryConfiguration {

    static ImmutableRepositoryConfiguration.Builder builder() {
        return ImmutableRepositoryConfiguration.builder();
    }

    /**
     * @return A repository configuration using default values.
     */
    static ImmutableRepositoryConfiguration usingDefaults() {
        return builder().build();
    }

    /**
     * @return The repository name suffix to use.
     */
    @Value.Default
    default String repositoryNameSuffix() {
        return "Repository";
    }

    /**
     * @return Should interfaces be generated for each repository?
     */
    @Value.Default
    default boolean repositoryGenerateInterface() {
        return true;
    }

}
