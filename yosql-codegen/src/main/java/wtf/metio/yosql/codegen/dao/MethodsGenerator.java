/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates methods for a repository.
 */
public interface MethodsGenerator {

    /**
     * Creates all method declarations of a repository based on a number of {@link SqlStatement}s
     * to be used in interfaces.
     *
     * @param statements The statements to use.
     * @return The method specifications based on the given statements.
     */
    Iterable<MethodSpec> asMethodsDeclarations(List<SqlStatement> statements);

    /**
     * Creates all methods of a repository based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return The method specifications based on the given statements.
     */
    Iterable<MethodSpec> asMethods(List<SqlStatement> statements);

}
