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
 * Generates Mutiny based methods.
 */
public interface MutinyMethodGenerator {

    /**
     * Creates a Mutiny based method for calling a procedure in a database.
     *
     * @param configuration    The configuration to use.
     * @param vendorStatements The vendor statements to use.
     * @return A Mutiny based call method.
     */
    MethodSpec mutinyCallMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

    /**
     * Creates a Mutiny based method for reading data out of a database.
     *
     * @param configuration    The configuration to use.
     * @param vendorStatements The vendor statements to use.
     * @return A Mutiny based read method.
     */
    MethodSpec mutinyReadMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

    /**
     * Creates a Mutiny based method for writing data out of a database.
     *
     * @param configuration    The configuration to use.
     * @param vendorStatements The vendor statements to use.
     * @return A Mutiny based write method.
     */
    MethodSpec mutinyWriteMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

}
