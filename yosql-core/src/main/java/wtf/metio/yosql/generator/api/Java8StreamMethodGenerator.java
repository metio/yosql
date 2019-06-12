/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.api;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;

import java.util.List;

/**
 * Generates Java 8 {@link java.util.stream.Stream} based methods.
 */
public interface Java8StreamMethodGenerator {

    /**
     * Creates an eager streaming method for reading data out of a database.
     *
     * @param configuration
     *            The configuration to use.
     * @param vendorStatements
     *            The vendor statements to use.
     * @return An eager streaming method.
     */
    MethodSpec streamEagerMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

    /**
     * Creates a lazy streaming method for reading data out of a database.
     *
     * @param configuration
     *            The configuration to use.
     * @param vendorStatements
     *            The vendor statements to use.
     * @return A lazy streaming method.
     */
    MethodSpec streamLazyMethod(SqlConfiguration configuration, List<SqlStatement> vendorStatements);

}
