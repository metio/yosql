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
 * High-level code generator that transform SQL statements into Java code.
 */
public interface CodeGenerator {

    /**
     * @param statements The statements to use.
     * @return The generated Java code.
     */
    Stream<PackagedTypeSpec> generateCode(List<SqlStatement> statements);

}
