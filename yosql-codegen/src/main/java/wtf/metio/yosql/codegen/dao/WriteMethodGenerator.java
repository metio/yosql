/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates WRITING methods.
 */
public interface WriteMethodGenerator {

    /**
     * Method declaration for a method that executes a write against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method declaration for a writing method.
     */
    MethodSpec writeMethodDeclaration(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates code that execute a write against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a writing method.
     */
    MethodSpec writeMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Method declaration for a method that executes a batched write against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method declaration for a batch writing method.
     */
    MethodSpec batchWriteMethodDeclaration(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates a batching write method.
     *
     * @param configuration    The configuration to use.
     * @param vendorStatements The vendor statements to use.
     * @return The batch method specification.
     */
    MethodSpec batchWriteMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

}
