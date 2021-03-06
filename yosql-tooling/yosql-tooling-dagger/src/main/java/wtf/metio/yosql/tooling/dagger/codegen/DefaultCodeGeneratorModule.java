/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.codegen;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.CodeGenerator;
import wtf.metio.yosql.codegen.api.RepositoryGenerator;
import wtf.metio.yosql.codegen.api.UtilitiesGenerator;
import wtf.metio.yosql.codegen.blocks.DefaultCodeGenerator;
import wtf.metio.yosql.tooling.dagger.codegen.blocks.DefaultGenericBlocksModule;
import wtf.metio.yosql.tooling.dagger.codegen.dao.DefaultDaoModule;
import wtf.metio.yosql.tooling.dagger.codegen.logging.DefaultLoggingModule;
import wtf.metio.yosql.tooling.dagger.codegen.utilities.DefaultUtilitiesModule;

import javax.inject.Singleton;

/**
 * Dagger module for all code generator related components.
 */
@Module(includes = {
        DefaultGenericBlocksModule.class,
        DefaultDaoModule.class,
        DefaultUtilitiesModule.class,
        DefaultLoggingModule.class
})
public class DefaultCodeGeneratorModule {

    @Provides
    @Singleton
    CodeGenerator provideCodeGenerator(
            final @Delegating RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilitiesGenerator) {
        return new DefaultCodeGenerator(repositoryGenerator, utilitiesGenerator);
    }

}
