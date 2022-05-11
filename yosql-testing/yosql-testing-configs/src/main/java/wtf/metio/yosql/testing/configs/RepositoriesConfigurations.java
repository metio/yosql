/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.configs;

import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;

public final class RepositoriesConfigurations {

    public static RepositoriesConfiguration defaults() {
        return RepositoriesConfiguration.usingDefaults().build();
    }

    public static RepositoriesConfiguration validatingMethodNames() {
        return RepositoriesConfiguration.copyOf(defaults())
                .withValidateMethodNamePrefixes(true);
    }

    public static RepositoriesConfiguration rxJavaOnly() {
        return RepositoriesConfiguration.copyOf(defaults())
                .withGenerateBatchApi(false)
                .withGenerateBlockingApi(false)
                .withGenerateMutinyApi(false)
                .withGenerateReactorApi(false)
                .withGenerateRxJavaApi(true)
                .withGenerateStreamApi(false);
    }

    private RepositoriesConfigurations() {
        // factory class
    }

}
