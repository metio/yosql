package wtf.metio.yosql.generator.helpers;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.dagger.Delegating;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.model.ExecutionConfiguration;

/**
 * Dagger module for the various helper classes
 */
@Module
public class HelperModule {

    @Provides
    TypicalCodeBlocks provideTypicalCodeBlocks(final ExecutionConfiguration config, final @Delegating LoggingGenerator logging) {
        return new TypicalCodeBlocks(config, logging);
    }

    @Provides
    TypicalFields provideTypicalFields(final AnnotationGenerator annotations) {
        return new TypicalFields(annotations);
    }

}
