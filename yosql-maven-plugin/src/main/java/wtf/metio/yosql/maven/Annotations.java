/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

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
    @Parameter(defaultValue = "true")
    private boolean annotateClasses;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated fields.
     */
    @Parameter(defaultValue = "false")
    private boolean annotateFields;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated methods.
     */
    @Parameter(defaultValue = "false")
    private boolean annotateMethods;

    /**
     * Sets the comment used for annotated classes.
     */
    @Parameter(defaultValue = "DO NOT EDIT")
    private String classComment;

    /**
     * Sets the comment used for annotated fields.
     */
    @Parameter(defaultValue = "DO NOT EDIT")
    private String fieldComment;

    /**
     * Sets the comment used for annotated methods.
     */
    @Parameter(defaultValue = "DO NOT EDIT")
    private String methodComment;

    /**
     * Controls which @Generated annotation should be used.
     */
    @Parameter(defaultValue = "ANNOTATION_API")
    private String api;

    public AnnotationConfiguration asConfiguration() {
        return AnnotationConfiguration.builder()
                .setClassComment(classComment)
                .setFieldComment(fieldComment)
                .setMethodComment(methodComment)
                .setClassAnnotation(AnnotationClassOptions.valueOf(api))
                .setFieldAnnotation(AnnotationClassOptions.valueOf(api))
                .setMethodAnnotation(AnnotationClassOptions.valueOf(api))
                .build();
    }

}
