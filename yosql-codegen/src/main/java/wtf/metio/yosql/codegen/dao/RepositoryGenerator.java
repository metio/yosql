/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
