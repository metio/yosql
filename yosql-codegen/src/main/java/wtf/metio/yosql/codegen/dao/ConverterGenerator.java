/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
