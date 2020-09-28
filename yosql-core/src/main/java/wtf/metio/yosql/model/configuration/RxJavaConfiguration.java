/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import com.squareup.javapoet.ClassName;

import javax.annotation.Nullable;
import java.util.Optional;

@AutoValue
public abstract class RxJavaConfiguration {

    public static Builder builder() {
        return new AutoValue_RxJavaConfiguration.Builder();
    }

    public abstract ClassName flowStateClass();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setFlowStateClass(ClassName value);

        public abstract RxJavaConfiguration build();

    }

}
