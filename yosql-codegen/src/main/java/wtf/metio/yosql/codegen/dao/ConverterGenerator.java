/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.stream.Stream;

/**
 * Generates converter related classes.
 */
public interface ConverterGenerator {

    /**
     * Creates converter related classes based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return A stream of type specifications and their target package.
     */
    Stream<PackagedTypeSpec> generateConverterClasses(List<SqlStatement> statements);

}
