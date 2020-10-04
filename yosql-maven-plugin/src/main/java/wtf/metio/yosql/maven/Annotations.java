/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

import javax.annotation.processing.Generated;

/**
 * Configures how annotations are applied to the generated code.
 */
public class Annotations {

    /**
     * Controls whether {@link Generated} annotations should be added to the generated classes.
     */
    private final boolean annotateClasses = true;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated fields.
     */
    private final boolean annotateFields = false;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated methods.
     */
    private final boolean annotateMethods = false;

    /**
     * Sets the comment used for annotated classes.
     */
    private final String classComment = "DO NOT EDIT";

    /**
     * Sets the comment used for annotated fields.
     */
    private final String fieldComment = "DO NOT EDIT";

    /**
     * Sets the comment used for annotated methods.
     */
    private final String methodComment = "DO NOT EDIT";

    /**
     * Controls which @Generated annotation should be used.
     */
    private final AnnotationClassOptions api = AnnotationClassOptions.ANNOTATION_API;

    /**
     * The name of the code generator
     */
    private final String generatorName = "YoSQL";

    private final AnnotationMemberOptions classMembers = AnnotationMemberOptions.WITHOUT_DATE;
    private final AnnotationMemberOptions methodMembers = AnnotationMemberOptions.WITHOUT_DATE;
    private final AnnotationMemberOptions fieldMembers = AnnotationMemberOptions.WITHOUT_DATE;

    public AnnotationConfiguration asConfiguration() {
        return AnnotationConfiguration.builder()
                .setClassComment(classComment)
                .setFieldComment(fieldComment)
                .setMethodComment(methodComment)
                .setClassAnnotation(api)
                .setFieldAnnotation(api)
                .setMethodAnnotation(api)
                .setClassMembers(classMembers)
                .setMethodMembers(methodMembers)
                .setFieldMembers(fieldMembers)
                .setGeneratorName(generatorName)
                .build();
    }

}
