/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class RepositoryConfiguration {

    public static Builder builder() {
        return new AutoValue_RepositoryConfiguration.Builder();
    }

    public abstract String repositoryNameSuffix();

    public abstract boolean repositoryGenerateInterface();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setRepositoryNameSuffix(String repositoryNameSuffix);

        public abstract Builder setRepositoryGenerateInterface(boolean repositoryGenerateInterface);

        public abstract RepositoryConfiguration build();

    }

}
