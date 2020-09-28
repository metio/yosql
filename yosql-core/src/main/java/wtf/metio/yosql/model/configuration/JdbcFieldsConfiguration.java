/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class JdbcFieldsConfiguration {

    public static Builder builder() {
        return new AutoValue_JdbcFieldsConfiguration.Builder();
    }

    public abstract String rawSuffix();
    public abstract String indexSuffix();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setRawSuffix(String value);
        public abstract Builder setIndexSuffix(String value);

        public abstract JdbcFieldsConfiguration build();

    }

}
