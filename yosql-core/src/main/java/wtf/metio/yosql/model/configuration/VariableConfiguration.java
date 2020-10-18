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

/**
 * Configures the various variable related options.
 */
@Value.Immutable
public interface VariableConfiguration {

    static ImmutableVariableConfiguration.Builder builder() {
        return ImmutableVariableConfiguration.builder();
    }

    /**
     * @return A variable configuration using default values.
     */
    static ImmutableVariableConfiguration usingDefaults() {
        return builder().build();
    }

    @Value.Default
    default VariableTypeOptions variableType() {
        return VariableTypeOptions.TYPE;
    }

    @Value.Default
    default List<Modifier> modifiers() {
        return List.of(Modifier.FINAL);
    }

}
