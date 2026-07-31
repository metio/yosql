/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates repositories.
 */
public interface RepositoryGenerator {

    /**
     * Generates a single repository class.
     *
     * @param repositoryName The fully-qualified name of the repository to generate.
     * @param statements     The statements to include in the repository.
     * @return The repository type specification and its intended target package.
     */
    PackagedTypeSpec generateRepositoryClass(String repositoryName, List<SqlStatement> statements);

    /**
     * Generates a single repository interface.
     *
     * @param repositoryName The fully-qualified name of the repository interface to generate.
     * @param statements     The statements to include in the repository.
     * @return The repository type specification and its intended target package.
     */
    PackagedTypeSpec generateRepositoryInterface(String repositoryName, List<SqlStatement> statements);

}
