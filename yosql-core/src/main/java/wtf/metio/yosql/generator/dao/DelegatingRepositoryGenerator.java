/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao;

import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.internal.PackagedTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

final class DelegatingRepositoryGenerator implements RepositoryGenerator {

    private final RuntimeConfiguration runtimeConfiguration;
    private final RepositoryGenerator jdbcRepositoryGenerator;
    private final RepositoryGenerator r2dbcRepositoryGenerator;
    private final RepositoryGenerator springJdbcRepositoryGenerator;
    private final RepositoryGenerator springDataJdbcRepositoryGenerator;

    DelegatingRepositoryGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final RepositoryGenerator jdbcRepositoryGenerator,
            final RepositoryGenerator r2dbcRepositoryGenerator,
            final RepositoryGenerator springDataJdbcRepositoryGenerator,
            final RepositoryGenerator springJdbcRepositoryGenerator) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.jdbcRepositoryGenerator = jdbcRepositoryGenerator;
        this.r2dbcRepositoryGenerator = r2dbcRepositoryGenerator;
        this.springJdbcRepositoryGenerator = springJdbcRepositoryGenerator;
        this.springDataJdbcRepositoryGenerator = springDataJdbcRepositoryGenerator;
    }

    @Override
    public PackagedTypeSpec generateRepository(final String repositoryName, final List<SqlStatement> statements) {
        return dao().generateRepository(repositoryName, statements);
    }

    private RepositoryGenerator dao() {
        return switch (runtimeConfiguration.api().daoApi()) {
            case R2DBC -> r2dbcRepositoryGenerator;
            case SPRING_DATA_JDBC -> springDataJdbcRepositoryGenerator;
            case SPRING_JDBC -> springJdbcRepositoryGenerator;
            default -> jdbcRepositoryGenerator;
        };
    }

}
