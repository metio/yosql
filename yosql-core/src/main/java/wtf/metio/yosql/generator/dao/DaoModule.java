/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao;

import dagger.Provides;
import dagger.Module;
import wtf.metio.yosql.dagger.Delegating;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.dao.jdbc.DaoJdbcModule;
import wtf.metio.yosql.generator.dao.jdbc.JDBC;

/**
 * Dagger module for the DAO API.
 */
@Module(includes = DaoJdbcModule.class)
public final class DaoModule {

    @Delegating
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @JDBC RepositoryGenerator jdbcRepositoryGenerator) {
        return new DelegatingRepositoryGenerator(jdbcRepositoryGenerator);
    }

}
