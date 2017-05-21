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
 * Generates RxJava2 based methods.
 */
@FunctionalInterface
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
