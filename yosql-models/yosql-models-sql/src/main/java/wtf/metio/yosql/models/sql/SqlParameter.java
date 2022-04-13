/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.models.sql;

import org.immutables.value.Value;

import java.util.Optional;

/**
 * Represents a single input parameter of a SQL statement.
 */
@Value.Immutable
public interface SqlParameter {

    static ImmutableSqlParameter.Builder builder() {
        return ImmutableSqlParameter.builder();
    }

    static ImmutableSqlParameter copy(final SqlParameter parameter) {
        return ImmutableSqlParameter.copyOf(parameter);
    }

    /**
     * @return The name of the parameter.
     */
    String name();

    /**
     * @return The fully-qualified type name.
     */
    String type();

    /**
     * @return The fully-qualified name of the converter to use.
     */
    Optional<String> converter();

    /**
     * @return The indices in the SQL statement that match this parameter.
     */
    int[] indices();
    
    /**
     * @return <code>true</code> in case this statement has indices, <code>false</code> otherwise.
     */
    @Value.Lazy
    default boolean hasIndices() {
        return indices() != null && indices().length > 0;
    }

}
