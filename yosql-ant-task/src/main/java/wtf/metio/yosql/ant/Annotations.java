/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.ant;

import javax.annotation.processing.Generated;
import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

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
    private final String classComment = AnnotationConfiguration.DEFAULT_COMMENT;

    /**
     * Sets the comment used for annotated fields.
     */
    private final String fieldComment = AnnotationConfiguration.DEFAULT_COMMENT;

    /**
     * Sets the comment used for annotated methods.
     */
    private final String methodComment = AnnotationConfiguration.DEFAULT_COMMENT;

    /**
     * Controls which @Generated annotation should be used.
     */
    private final AnnotationClassOptions api = AnnotationConfiguration.DEFAULT_CLASS_OPTION;

    /**
     * The name of the code generator
     */
    private final String generatorName = AnnotationConfiguration.DEFAULT_GENERATOR_NAME;

    private final AnnotationMemberOptions classMembers = AnnotationConfiguration.DEFAULT_MEMBER_OPTION;
    private final AnnotationMemberOptions methodMembers = AnnotationConfiguration.DEFAULT_MEMBER_OPTION;
    private final AnnotationMemberOptions fieldMembers = AnnotationConfiguration.DEFAULT_MEMBER_OPTION;

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
