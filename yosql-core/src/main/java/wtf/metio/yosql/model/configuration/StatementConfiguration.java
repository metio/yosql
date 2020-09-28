/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.StatementInRepositoryOptions;

@AutoValue
public abstract class StatementConfiguration {

    public static Builder builder() {
        return new AutoValue_StatementConfiguration.Builder();
    }

    // TODO: move to RepositoryConfig?
    public abstract StatementInRepositoryOptions embed();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setEmbed(StatementInRepositoryOptions targetVersion);

        public abstract StatementConfiguration build();

    }

}
