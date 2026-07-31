/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generator for class constructors.
 */
public interface ConstructorGenerator {

    /**
     * Creates the constructor block for a repository based on a number of {@link SqlStatement}s.
     *
     * @param statements The statements to use.
     * @return The constructor for a repository.
     */
    MethodSpec repository(List<SqlStatement> statements);

}
