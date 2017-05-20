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
 * Generates batch methods that take in a multitude of inputs and produce multiple outputs.
 */
public interface BatchMethodGenerator {

    /**
     * Generates a batching write method.
     *
     * @param configuration
     *            The configuration to use.
     * @param vendorStatements
     *            The vendor statements to use.
     * @return The batch method specification.
     */
    MethodSpec batchWriteMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

}
