/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao;

import wtf.metio.yosql.model.PackageTypeSpec;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.dao.jdbc.JDBC;

import java.util.List;

final class DelegatingRepositoryGenerator implements RepositoryGenerator {

    private final RepositoryGenerator jdbcRepositoryGenerator;

    DelegatingRepositoryGenerator(
            final @JDBC RepositoryGenerator jdbcRepositoryGenerator) {
        this.jdbcRepositoryGenerator = jdbcRepositoryGenerator;
    }

    @Override
    public PackageTypeSpec generateRepository(final String repositoryName, final List<SqlStatement> statements) {
        final PackageTypeSpec repository = jdbcRepositoryGenerator.generateRepository(repositoryName, statements);
        return repository;
    }

}
