/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.configuration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(
        as = ImmutableParameterConverter.class
)
@JsonDeserialize(
        as = ImmutableParameterConverter.class
)
public interface ParameterConverter {

    static ImmutableParameterConverter.AliasBuildStage builder() {
        return ImmutableParameterConverter.builder();
    }

    String alias();

    String converterType();

    String methodName();

    String parameterType();

}
