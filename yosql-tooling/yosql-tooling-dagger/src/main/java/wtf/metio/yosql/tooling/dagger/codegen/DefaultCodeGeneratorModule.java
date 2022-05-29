/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.codegen;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.dao.CodeGenerator;
import wtf.metio.yosql.codegen.dao.ConverterGenerator;
import wtf.metio.yosql.codegen.dao.DefaultCodeGenerator;
import wtf.metio.yosql.codegen.dao.RepositoryGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.codegen.blocks.DefaultGenericBlocksModule;
import wtf.metio.yosql.tooling.dagger.codegen.dao.DefaultDaoModule;
import wtf.metio.yosql.tooling.dagger.codegen.logging.DefaultLoggingModule;

import javax.inject.Singleton;

/**
 * Dagger module for all code generator related components.
 */
@Module(includes = {
        DefaultGenericBlocksModule.class,
        DefaultDaoModule.class,
        DefaultLoggingModule.class
})
public class DefaultCodeGeneratorModule {

    @Provides
    @Singleton
    CodeGenerator provideCodeGenerator(
            final RepositoryGenerator repositoryGenerator,
            final ConverterGenerator converterGenerator,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultCodeGenerator(repositoryGenerator, converterGenerator, runtimeConfiguration.repositories());
    }

}
