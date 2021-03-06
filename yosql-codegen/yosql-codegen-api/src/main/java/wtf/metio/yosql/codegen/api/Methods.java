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
 * Generator for {@link MethodSpec}s.
 */
public interface Methods {

    MethodSpec.Builder publicMethod(String name);

    /**
     * Prepare {@link MethodSpec} with Javadoc
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder publicMethod(String name, List<SqlStatement> statements);

    MethodSpec.Builder implementation(String name);

    MethodSpec.Builder constructor();

}
