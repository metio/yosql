/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.dao;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.DelegatingRepositoryGenerator;
import wtf.metio.yosql.codegen.api.RepositoryGenerator;
import wtf.metio.yosql.dao.jdbc.Jdbc;
import wtf.metio.yosql.dao.r2dbc.R2DBC;
import wtf.metio.yosql.dao.spring.data.jdbc.SpringDataJDBC;
import wtf.metio.yosql.dao.spring.jdbc.SpringJDBC;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the DAO APIs.
 */
@Module(includes = {
        JdbcDaoModule.class,
        R2dbcDaoModule.class,
        SpringDataJdbcDaoModule.class,
        SpringJdbcDaoModule.class
})
public class DefaultDaoModule {

    @Provides
    @Delegating
    public RepositoryGenerator provideRepositoryGenerator(
            final RuntimeConfiguration runtime,
            final @Jdbc RepositoryGenerator jdbcRepositoryGenerator,
            final @R2DBC RepositoryGenerator r2dbcRepositoryGenerator,
            final @SpringDataJDBC RepositoryGenerator springDataJdbcRepositoryGenerator,
            final @SpringJDBC RepositoryGenerator springJdbcRepositoryGenerator) {
        return new DelegatingRepositoryGenerator(
                runtime.api(),
                jdbcRepositoryGenerator,
                r2dbcRepositoryGenerator,
                springDataJdbcRepositoryGenerator,
                springJdbcRepositoryGenerator);
    }

}
