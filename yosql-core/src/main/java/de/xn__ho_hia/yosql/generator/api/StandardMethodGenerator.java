/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.api;

import java.util.List;

import com.squareup.javapoet.MethodSpec;

import de.xn__ho_hia.yosql.model.SqlConfiguration;
import de.xn__ho_hia.yosql.model.SqlStatement;

/**
 * Generates 'standard' method - implementation decides whatever that means.
 */
public interface StandardMethodGenerator {

    /**
     * @param methodName
     * @param configuration
     * @param vendorStatements
     * @return A method specification for a standard reading method.
     */
    MethodSpec standardReadMethod(String methodName, SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * @param methodName
     * @param configuration
     * @param vendorStatements
     * @return A method specification for a standard writing method.
     */
    MethodSpec standardWriteMethod(String methodName, SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * @param methodName
     * @param configuration
     * @param vendorStatements
     * @return A method specification for a standard calling method.
     */
    MethodSpec standardCallMethod(String methodName, SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

}
