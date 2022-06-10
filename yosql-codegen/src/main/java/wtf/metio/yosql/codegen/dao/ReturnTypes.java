/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Optional;

/**
 * Determines which return type should be used for a given {@link SqlConfiguration}.
 */
public interface ReturnTypes {

    /**
     * Determine return type for a method based on the configured
     * {@link wtf.metio.yosql.models.configuration.ReturningMode} of a {@link SqlConfiguration}.
     *
     * @param configuration The configuration to use.
     * @return The fully qualified return type.
     */
    Optional<TypeName> resultType(SqlConfiguration configuration);

    /**
     * Determine return type for a method that returns a single result.
     *
     * @param configuration The configuration to use.
     * @return The fully qualified return type.
     */
    TypeName singleResultType(SqlConfiguration configuration);

    /**
     * Determine return type for a method that returns multiple results.
     *
     * @param configuration The configuration to use.
     * @return The fully qualified return type.
     */
    TypeName multiResultType(SqlConfiguration configuration);

    /**
     * Determine return type for a method that returns a cursor result.
     *
     * @param configuration The configuration to use.
     * @return The fully qualified return type.
     */
    TypeName cursorResultType(SqlConfiguration configuration);

}
