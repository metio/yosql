/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.api;

import wtf.metio.yosql.tooling.codegen.model.internal.PackagedTypeSpec;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlStatement;

import java.util.List;

/**
 * Generates repositories.
 */
@FunctionalInterface
public interface RepositoryGenerator {

    /**
     * Generates a single repository.
     *
     * @param repositoryName The fully-qualified name of the repository to generate.
     * @param statements     The statements to include in the repository.
     * @return The repository type specification and its intended target package.
     */
    PackagedTypeSpec generateRepository(String repositoryName, List<SqlStatement> statements);

}
