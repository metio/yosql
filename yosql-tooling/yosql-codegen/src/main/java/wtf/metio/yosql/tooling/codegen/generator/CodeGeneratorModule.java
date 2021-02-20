/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.tooling.codegen.generator.api.CodeGenerator;
import wtf.metio.yosql.tooling.codegen.generator.api.RepositoryGenerator;
import wtf.metio.yosql.tooling.codegen.generator.api.UtilitiesGenerator;
import wtf.metio.yosql.tooling.codegen.generator.blocks.BlocksModule;
import wtf.metio.yosql.tooling.codegen.generator.dao.DaoModule;
import wtf.metio.yosql.tooling.codegen.generator.logging.LoggingModule;
import wtf.metio.yosql.tooling.codegen.generator.utilities.UtilitiesModule;
import wtf.metio.yosql.tooling.codegen.model.annotations.Delegating;

import javax.inject.Singleton;

/**
 * Dagger module for all code generator related components.
 */
@Module(includes = {
        DaoModule.class,
        UtilitiesModule.class,
        BlocksModule.class,
        LoggingModule.class
})
public final class CodeGeneratorModule {

    @Provides
    @Singleton
    CodeGenerator provideCodeGenerator(
            final @Delegating RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilitiesGenerator) {
        return new DefaultCodeGenerator(repositoryGenerator, utilitiesGenerator);
    }

}
