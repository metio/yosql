/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.CodeGenerator;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.api.UtilitiesGenerator;
import wtf.metio.yosql.generator.blocks.BlocksModule;
import wtf.metio.yosql.generator.dao.DaoModule;
import wtf.metio.yosql.generator.logging.LoggingModule;
import wtf.metio.yosql.generator.utilities.UtilitiesModule;
import wtf.metio.yosql.model.annotations.Delegating;

import javax.inject.Singleton;

@Module(includes = {
        DaoModule.class,
        UtilitiesModule.class,
        BlocksModule.class,
        LoggingModule.class
})
public class CodeGeneratorModule {

    @Provides
    @Singleton
    CodeGenerator provideCodeGenerator(
            final @Delegating RepositoryGenerator repositoryGenerator,
            final UtilitiesGenerator utilitiesGenerator) {
        return new DefaultCodeGenerator(repositoryGenerator, utilitiesGenerator);
    }

}
