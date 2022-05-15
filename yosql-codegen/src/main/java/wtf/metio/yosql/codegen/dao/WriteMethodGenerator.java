/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates WRITING methods.
 */
public interface WriteMethodGenerator {

    /**
     * Generates code that execute a write against the database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a writing method.
     */
    MethodSpec writeMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates a batching write method.
     *
     * @param configuration    The configuration to use.
     * @param vendorStatements The vendor statements to use.
     * @return The batch method specification.
     */
    MethodSpec batchWriteMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

}
