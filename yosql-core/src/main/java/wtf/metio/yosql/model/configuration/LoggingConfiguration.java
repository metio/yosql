/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.LoggingApiOptions;

@AutoValue
public abstract class LoggingConfiguration {

    public static Builder builder() {
        return new AutoValue_LoggingConfiguration.Builder();
    }

    /**
     * @return Whether log statements should be generated
     */
    public final boolean shouldLog() {
        return LoggingApiOptions.NONE != api();
    }

    public abstract LoggingApiOptions api();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setApi(LoggingApiOptions api);

        public abstract LoggingConfiguration build();

    }

}
