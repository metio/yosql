/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlConfiguration;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlStatement;

import java.util.List;

/**
 * Generates RxJava2 based methods.
 */
public interface RxJavaMethodGenerator {

    /**
     * Creates a RxJava2 based method for reading data out of a database.
     *
     * @param configuration
     *            The configuration to use.
     * @param vendorStatements
     *            The vendor statements to use.
     * @return A RxJava2 based read method.
     */
    MethodSpec rxJava2ReadMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

}
