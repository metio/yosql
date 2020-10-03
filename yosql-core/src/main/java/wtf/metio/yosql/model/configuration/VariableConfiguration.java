/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.configuration;

import org.immutables.value.Value;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;
import java.util.List;

@Value.Immutable
public interface VariableConfiguration {

    static VariableConfiguration.Builder builder() {
        return new VariableConfiguration.Builder();
    }

    VariableTypeOptions variableType();

    List<Modifier> modifiers();

    class Builder extends ImmutableVariableConfiguration.Builder {
    }

}
