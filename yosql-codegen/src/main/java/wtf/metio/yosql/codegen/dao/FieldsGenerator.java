/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Optional;

/**
 * Generates fields and static initializers for those.
 */
public interface FieldsGenerator {

    /**
     * Creates the static initializer block for a class based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return The static initializer for the given statements.
     */
    Optional<CodeBlock> staticInitializer(List<SqlStatement> statements);

    /**
     * Creates the field specifications for a class based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return The field specifications.
     */
    Iterable<FieldSpec> asFields(List<SqlStatement> statements);

    /**
     * Generate the field name for the String constant holding the SQL statement.
     *
     * @param configuration The configuration of the SQL statement.
     * @return The name of the constant field.
     */
    String constantSqlStatementFieldName(SqlConfiguration configuration);

    /**
     * Generate the field name for the String constant holding the raw SQL statement.
     *
     * @param configuration The configuration of the SQL statement.
     * @return The name of the constant field.
     */
    String constantRawSqlStatementFieldName(SqlConfiguration configuration);

    /**
     * Generate the field name for the String constant holding the parameter indices of an SQL statement.
     *
     * @param configuration The configuration of the SQL statement.
     * @return The name of the constant field.
     */
    String constantSqlStatementParameterIndexFieldName(SqlConfiguration configuration);

}
