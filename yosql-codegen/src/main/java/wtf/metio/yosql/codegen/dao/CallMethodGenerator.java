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
 * Generates CALLING methods.
 */
public interface CallMethodGenerator {

    /**
     * Method declaration for a method that executes a call against a database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method declaration for a calling method.
     */
    MethodSpec callMethodDeclaration(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

    /**
     * Generates code that execute a call against the database.
     *
     * @param configuration    The configuration for the generated method.
     * @param vendorStatements The vendor statements for the generated method.
     * @return A method specification for a calling method.
     */
    MethodSpec callMethod(
            SqlConfiguration configuration,
            List<SqlStatement> vendorStatements);

}
