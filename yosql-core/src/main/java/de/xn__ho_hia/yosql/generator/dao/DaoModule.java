/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.dao;

import dagger.Module;
import dagger.Provides;
import de.xn__ho_hia.yosql.dagger.Delegating;
import de.xn__ho_hia.yosql.generator.api.RepositoryGenerator;
import de.xn__ho_hia.yosql.generator.dao.jdbc.DaoJdbcModule;
import de.xn__ho_hia.yosql.generator.dao.jdbc.JDBC;

/**
 * Dagger2 module for the DAO API.
 */
@Module(includes = DaoJdbcModule.class)
@SuppressWarnings("static-method")
public final class DaoModule {

    @Delegating
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @JDBC RepositoryGenerator jdbcRepositoryGenerator) {
        return new DelegatingRepositoryGenerator(jdbcRepositoryGenerator);
    }

}
