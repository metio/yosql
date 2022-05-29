/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generator for {@link MethodSpec}s.
 */
public interface Methods {

    /**
     * Create a new builder for public methods.
     *
     * @param name The name of the method.
     * @return A method builder preconfigured for public methods.
     */
    MethodSpec.Builder publicMethod(String name);

    /**
     * Create a new builder for public methods.
     *
     * @param name          The name of the method.
     * @param statements    The name of the method.
     * @param configuration The name of the method.
     * @return A method builder preconfigured for public methods.
     */
    MethodSpec.Builder publicMethod(String name, List<SqlStatement> statements, String configuration);

    /**
     * Create a new builder for method declarations.
     *
     * @param name          The name of the method.
     * @param statements    The name of the method.
     * @param configuration The name of the method.
     * @return A method builder preconfigured for public methods.
     */
    MethodSpec.Builder declaration(String name, List<SqlStatement> statements, String configuration);

    /**
     * Create a new builder for implementing methods.
     *
     * @param name The name of the method.
     * @return A method builder preconfigured for implementing methods.
     */
    MethodSpec.Builder implementation(String name);

    /**
     * Create a new builder for constructors.
     *
     * @return A method builder preconfigured for constructors.
     */
    MethodSpec.Builder constructor();

}
