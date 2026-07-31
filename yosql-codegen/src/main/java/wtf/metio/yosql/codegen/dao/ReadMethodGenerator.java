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
 * Generates READING methods.
 */
public interface ReadMethodGenerator {

    /**
     * Method declaration for a method that executes a read against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method declaration for a reading method.
     */
    MethodSpec readMethodDeclaration(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates code that executes a read against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a reading method.
     */
    MethodSpec readMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

}
