/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;

import java.nio.file.Path;

@AutoValue
public abstract class NameConfiguration {

    public static Builder builder() {
        return new AutoValue_NameConfiguration.Builder();
    }

    public abstract String utilityPackageName();

    public abstract String converterPackageName();

    public abstract String basePackageName();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setUtilityPackageName(String utilityPackageName);
        public abstract Builder setConverterPackageName(String converterPackageName);
        public abstract Builder setBasePackageName(String basePackageName);

        public abstract NameConfiguration build();

    }

}
