/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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

    /**
     * Create a new builder for public methods.
     *
     * @param name The name of the method.
     * @return A method builder preconfigured for public methods.
     */
    MethodSpec.Builder publicMethod(String name);

    /**
     * Prepare {@link MethodSpec} with Javadoc for blocking methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder blockingMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for batch methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder batchMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for eager stream methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder streamEagerMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for lazy stream methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder streamLazyMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for rxjava methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder rxJavaMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for reactor methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder reactorMethod(String name, List<SqlStatement> statements);

    /**
     * Prepare {@link MethodSpec} with Javadoc for mutiny methods.
     *
     * @param name       The name of the generated method.
     * @param statements The SQL statements used for Javadocs
     * @return The resulting builder for the MethodSpec.
     */
    MethodSpec.Builder mutinyMethod(String name, List<SqlStatement> statements);

    MethodSpec.Builder implementation(String name);

    MethodSpec.Builder constructor();

}
