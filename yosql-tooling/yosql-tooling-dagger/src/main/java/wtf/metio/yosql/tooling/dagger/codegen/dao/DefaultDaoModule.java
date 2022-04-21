/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.dao;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.DelegatingRepositoryGenerator;
import wtf.metio.yosql.codegen.api.RepositoryGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import java.util.Set;

/**
 * Dagger module for the DAO APIs.
 */
@Module(includes = {
        EBeanDaoModule.class,
        FluentJdbcDaoModule.class,
        JdbcDaoModule.class,
        JdbiDaoModule.class,
        JooqDaoModule.class,
        JpaDaoModule.class,
        MyBatisDaoModule.class,
        PyranidDaoModule.class,
        R2dbcDaoModule.class,
        SansOrmDaoModule.class,
        SpringDataJdbcDaoModule.class,
        SpringDataJpaDaoModule.class,
        SpringDataR2dbcDaoModule.class,
        SpringJdbcDaoModule.class
})
public class DefaultDaoModule { // TODO: rename to DefaultPersistenceModule

    @Provides
    @Delegating
    public RepositoryGenerator provideRepositoryGenerator(
            final RuntimeConfiguration runtime,
            final Set<RepositoryGenerator> repositoryGenerators) {
        return new DelegatingRepositoryGenerator(
                runtime.api(),
                repositoryGenerators);
    }

}
