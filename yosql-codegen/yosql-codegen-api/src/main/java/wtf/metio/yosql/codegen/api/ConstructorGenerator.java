/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generator for class constructors.
 */
public interface ConstructorGenerator {

    /**
     * Creates the constructor block for a repository based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return The constructor for a repository.
     */
    MethodSpec forRepository(List<SqlStatement> statements);

}
