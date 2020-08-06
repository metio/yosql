package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;

import javax.annotation.processing.Generated;

/**
 * Configures how annotations are applied to the generated code.
 */
public class Annotations {

    /**
     * Controls whether {@link Generated} annotations should be added to the generated classes.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean annotateClasses;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated fields.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean annotateFields;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated methods.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean annotateMethods;

    /**
     * Sets the comment used for annotated classes.
     */
    @Parameter(required = true, defaultValue = "DO NOT EDIT")
    private String classComment;

    /**
     * Sets the comment used for annotated fields.
     */
    @Parameter(required = true, defaultValue = "DO NOT EDIT")
    private String fieldComment;

    /**
     * Sets the comment used for annotated methods.
     */
    @Parameter(required = true, defaultValue = "DO NOT EDIT")
    private String methodComment;
    
    public AnnotationConfiguration asConfiguration() {
        return AnnotationConfiguration.builder()
                .setClassComment(classComment)
                .setFieldComment(fieldComment)
                .setMethodComment(methodComment)
                .setClassAnnotation(AnnotationClassOptions.ANNOTATION_API) // TODO: configure w/ Maven
                .setFieldAnnotation(AnnotationClassOptions.ANNOTATION_API) // TODO: configure w/ Maven
                .setMethodAnnotation(AnnotationClassOptions.ANNOTATION_API) // TODO: configure w/ Maven
                .build();
    }

}
