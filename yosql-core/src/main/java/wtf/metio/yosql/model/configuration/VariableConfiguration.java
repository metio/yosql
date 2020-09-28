/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import com.google.auto.value.AutoValue;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;
import java.util.List;

@AutoValue
public abstract class VariableConfiguration {

    public static Builder builder() {
        return new AutoValue_VariableConfiguration.Builder();
    }

    // TODO: align with JavaConfiguration#useVar
    public abstract VariableTypeOptions variableType();

    public abstract List<Modifier> modifiers();

    @AutoValue.Builder
    public abstract static class Builder {

        public abstract Builder setVariableType(VariableTypeOptions variableType);

        public abstract Builder setModifiers(List<Modifier> modifiers);

        public abstract VariableConfiguration build();

    }

}
