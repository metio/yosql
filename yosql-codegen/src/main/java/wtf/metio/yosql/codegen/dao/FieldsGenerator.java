/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
