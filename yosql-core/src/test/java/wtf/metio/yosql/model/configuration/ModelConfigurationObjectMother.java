package wtf.metio.yosql.model.configuration;

import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

import static wtf.metio.yosql.model.options.AnnotationMemberOptions.WITHOUT_DATE;

public final class ModelConfigurationObjectMother {

    public static AnnotationConfiguration annotationConfig(final AnnotationClassOptions classOptions) {
        return annotationConfig(classOptions, WITHOUT_DATE);
    }

    public static AnnotationConfiguration annotationConfig(
            final AnnotationClassOptions classOptions,
            final AnnotationMemberOptions memberOptions) {
        return AnnotationConfiguration.builder()
                .setClassAnnotation(classOptions)
                .setFieldAnnotation(classOptions)
                .setMethodAnnotation(classOptions)
                .setClassComment("class")
                .setFieldComment("field")
                .setMethodComment("method")
                .setGeneratorName("test")
                .setClassMembers(memberOptions)
                .setFieldMembers(memberOptions)
                .setMethodMembers(memberOptions)
                .build();
    }


    private ModelConfigurationObjectMother() {
        // factory class
    }

}
