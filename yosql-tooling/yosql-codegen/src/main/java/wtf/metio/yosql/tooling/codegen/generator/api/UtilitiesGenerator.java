/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.api;

import wtf.metio.yosql.tooling.codegen.model.internal.PackagedTypeSpec;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

/**
 * Generates utility classes.
 */
@FunctionalInterface
public interface UtilitiesGenerator {

    /**
     * Creates utilities based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return A stream of utility type specifications and their target package.
     */
    Stream<PackagedTypeSpec> generateUtilities(List<SqlStatement> statements);

}
