/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates 'blocking' methods as opposed to reactive methods.
 */
public interface BlockingMethodGenerator {

    /**
     * Generates code that executes a blocking read against the database using the configured API.
     * The <code>javax.sql</code> package for example calls
     * {@link java.sql.PreparedStatement#executeQuery()}.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a generic reading method.
     */
    MethodSpec blockingReadMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates code that execute a blocking write against the database using the configured API.
     * The <code>javax.sql</code> package for example calls
     * {@link java.sql.PreparedStatement#executeUpdate()}.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a generic writing method.
     */
    MethodSpec blockingWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates code that execute a blocking call against the database using the configured API.
     * The <code>javax.sql</code> package for example calls
     * {@link java.sql.CallableStatement#executeQuery()}.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a generic calling method.
     */
    MethodSpec blockingCallMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

}
