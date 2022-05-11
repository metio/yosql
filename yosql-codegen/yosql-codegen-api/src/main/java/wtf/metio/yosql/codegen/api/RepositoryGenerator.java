/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Generates repositories.
 */
public interface RepositoryGenerator {

    /**
     * Check whether this generator supports the given persistence API.
     *
     * @param api The API to check.
     * @return true if this generator supports the given API, false otherwise.
     */
    boolean supports(PersistenceApis api);

    /**
     * Generates a single repository.
     *
     * @param repositoryName The fully-qualified name of the repository to generate.
     * @param statements     The statements to include in the repository.
     * @return The repository type specification and its intended target package.
     */
    PackagedTypeSpec generateRepository(String repositoryName, List<SqlStatement> statements);

}
