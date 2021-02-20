/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.dao;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.tooling.codegen.generator.api.RepositoryGenerator;
import wtf.metio.yosql.tooling.codegen.generator.dao.jdbc.DAO_JDBC_Module;
import wtf.metio.yosql.tooling.codegen.generator.dao.jdbc.JDBC;
import wtf.metio.yosql.tooling.codegen.generator.dao.r2dbc.DAO_R2DBC_Module;
import wtf.metio.yosql.tooling.codegen.generator.dao.r2dbc.R2DBC;
import wtf.metio.yosql.tooling.codegen.generator.dao.spring_data_jdbc.DAO_Spring_Data_JDBC_Module;
import wtf.metio.yosql.tooling.codegen.generator.dao.spring_data_jdbc.Spring_Data_JDBC;
import wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc.DAO_Spring_JDBC_Module;
import wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc.Spring_JDBC;
import wtf.metio.yosql.tooling.codegen.model.annotations.Delegating;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;

/**
 * Dagger module for the DAO API.
 */
@Module(includes = {
        DAO_JDBC_Module.class,
        DAO_R2DBC_Module.class,
        DAO_Spring_Data_JDBC_Module.class,
        DAO_Spring_JDBC_Module.class
})
public final class DaoModule {

    @Provides
    @Delegating
    RepositoryGenerator provideRepositoryGenerator(
            final RuntimeConfiguration runtime,
            @JDBC final RepositoryGenerator jdbcRepositoryGenerator,
            @R2DBC final RepositoryGenerator r2dbcRepositoryGenerator,
            @Spring_Data_JDBC final RepositoryGenerator springDataJdbcRepositoryGenerator,
            @Spring_JDBC final RepositoryGenerator springJdbcRepositoryGenerator) {
        return new DelegatingRepositoryGenerator(
                runtime,
                jdbcRepositoryGenerator,
                r2dbcRepositoryGenerator,
                springDataJdbcRepositoryGenerator,
                springJdbcRepositoryGenerator);
    }

}
