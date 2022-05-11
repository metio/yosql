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
import wtf.metio.yosql.codegen.api.ConverterGenerator;
import wtf.metio.yosql.codegen.api.DelegatingConverterGenerator;
import wtf.metio.yosql.codegen.api.DelegatingRepositoryGenerator;
import wtf.metio.yosql.codegen.api.RepositoryGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;
import java.util.Set;

/**
 * Dagger module for the DAO APIs.
 */
@Module(includes = {
        JdbcDaoModule.class,
        R2dbcDaoModule.class
})
public class DefaultDaoModule {

    @Provides
    @Delegating
    @Singleton
    RepositoryGenerator provideRepositoryGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Set<RepositoryGenerator> repositoryGenerators) {
        return new DelegatingRepositoryGenerator(runtimeConfiguration.api(), repositoryGenerators);
    }

    @Provides
    @Delegating
    @Singleton
    ConverterGenerator provideConverterGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Set<ConverterGenerator> converterGenerators) {
        return new DelegatingConverterGenerator(runtimeConfiguration.api(), converterGenerators);
    }

}
