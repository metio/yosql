package wtf.metio.yosql.generator;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.CodeGenerator;
import wtf.metio.yosql.generator.api.RepositoryGenerator;
import wtf.metio.yosql.generator.api.UtilitiesGenerator;
import wtf.metio.yosql.generator.blocks.BlocksModule;
import wtf.metio.yosql.generator.dao.DaoModule;
import wtf.metio.yosql.generator.logging.LoggingModule;
import wtf.metio.yosql.generator.utilities.DefaultUtilitiesModule;
import wtf.metio.yosql.model.annotations.Delegating;

import javax.inject.Singleton;

@Module(includes = {
        DaoModule.class,
        DefaultUtilitiesModule.class,
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
