/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.sql;

import org.immutables.value.Value;

@Value.Immutable
public interface ResultRowConverter {

    static ImmutableResultRowConverter.Builder builder() {
        return ImmutableResultRowConverter.builder();
    }

    String alias();

    String converterType();

    String methodName();

    String resultType();     // TODO: resultType should be resultRowType b/c then rxjava/reactor/stream/list are just converters that can be plugged in

}
