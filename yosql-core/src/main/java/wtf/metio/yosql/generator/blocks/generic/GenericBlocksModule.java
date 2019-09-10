package wtf.metio.yosql.generator.blocks.generic;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.configuration.VariableConfiguration;

@Module
public class GenericBlocksModule {

    @Provides
    AnnotationGenerator annotationGenerator(final RuntimeConfiguration runtime, final Translator translator) {
        return new DefaultAnnotationGenerator(runtime.annotations(), translator);
    }

    @Provides
    Classes classes() {
        return new DefaultClasses();
    }

    @Provides
    ControlFlows controlFlows(
            final Variables variables,
            final Names names) {
        return new DefaultControlFlows(variables, names);
    }

    @Provides
    Fields fields(final AnnotationGenerator annotations) {
        return new DefaultFields(annotations);
    }

    @Provides
    GenericBlocks genericBlocks() {
        return new DefaultGenericBlocks();
    }

    @Provides
    Methods methods() {
        return new DefaultMethods();
    }

    @Provides
    Names names() {
        return new DefaultNames();
    }

    @Provides
    Parameters parameters(final Names names) {
        return new DefaultParameters(names);
    }

    @Provides
    Variables variables(final VariableConfiguration options) {
        return new DefaultVariables(options);
    }

}
