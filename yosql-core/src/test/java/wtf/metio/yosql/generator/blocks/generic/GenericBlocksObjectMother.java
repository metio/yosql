package wtf.metio.yosql.generator.blocks.generic;

import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.*;

import static wtf.metio.yosql.i18n.I18nObjectMother.testTranslator;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.annotationConfig;
import static wtf.metio.yosql.model.configuration.ModelConfigurationObjectMother.variableConfiguration;
import static wtf.metio.yosql.model.options.AnnotationClassOptions.PROCESSING_API;

public final class GenericBlocksObjectMother {

    public static GenericBlocks genericBlocks() {
        return new DefaultGenericBlocks();
    }

    public static Names names() {
        return new DefaultNames();
    }

    public static Fields fields() {
        return new DefaultFields(annotationGenerator());
    }

    public static Parameters parameters() {
        return new DefaultParameters(names());
    }

    public static Variables variables() {
        return new DefaultVariables(variableConfiguration());
    }

    public static AnnotationGenerator annotationGenerator() {
        return new DefaultAnnotationGenerator(annotationConfig(PROCESSING_API), testTranslator());
    }

    private GenericBlocksObjectMother() {
        // factory class
    }

}
