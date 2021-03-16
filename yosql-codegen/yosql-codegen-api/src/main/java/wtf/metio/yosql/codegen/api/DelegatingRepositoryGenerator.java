/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.api;

import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;
import wtf.metio.yosql.models.immutables.PackagedTypeSpec;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

/**
 * Delegates its work to first implementation matching the configured {@link PersistenceApis}.
 */
public final class DelegatingRepositoryGenerator implements RepositoryGenerator {

    private final ApiConfiguration apis;
    private final List<RepositoryGenerator> generators;

    /**
     * @param apis       The API configuration.
     * @param generators The generators to delegate to.
     */
    public DelegatingRepositoryGenerator(
            final ApiConfiguration apis,
            final RepositoryGenerator... generators) {
        this.apis = apis;
        this.generators = List.of(generators);
    }

    @Override
    public boolean supports(final PersistenceApis api) {
        return generators.stream().anyMatch(generator -> generator.supports(api));
    }

    @Override
    public PackagedTypeSpec generateRepository(final String repositoryName, final List<SqlStatement> statements) {
        return dao().generateRepository(repositoryName, statements);
    }

    private RepositoryGenerator dao() {
        return generators.stream()
                .filter(generator -> generator.supports(apis.daoApi()))
                .findFirst()
                .orElseThrow();
    }

}
