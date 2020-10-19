/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.cli;

import picocli.CommandLine;
import wtf.metio.yosql.model.configuration.AnnotationConfiguration;
import wtf.metio.yosql.model.options.AnnotationClassOptions;
import wtf.metio.yosql.model.options.AnnotationMemberOptions;

/**
 * Configures how annotations are applied to the generated code.
 */
public class Annotations {

    @CommandLine.Option(
            names = "--annotations-class-comment",
            description = "Sets the comment used for annotated classes.",
            defaultValue = AnnotationConfiguration.DEFAULT_COMMENT)
    String classComment;

    @CommandLine.Option(
            names = "--annotations-field-comment",
            description = "Sets the comment used for annotated fields.",
            defaultValue = AnnotationConfiguration.DEFAULT_COMMENT)
    String fieldComment;

    @CommandLine.Option(
            names = "--annotations-method-comment",
            description = "Sets the comment used for annotated methods.",
            defaultValue = AnnotationConfiguration.DEFAULT_COMMENT)
    String methodComment;

    @CommandLine.Option(
            names = "--annotations-class-annotation",
            description = "Controls which @Generated annotation should be used for classes.",
            defaultValue = AnnotationConfiguration.DEFAULT_CLASS_OPTION_STRING)
    AnnotationClassOptions classAnnotation;

    @CommandLine.Option(
            names = "--annotations-field-annotation",
            description = "Controls which @Generated annotation should be used for fields.",
            defaultValue = AnnotationConfiguration.DEFAULT_CLASS_OPTION_STRING)
    AnnotationClassOptions fieldAnnotation;

    @CommandLine.Option(
            names = "--annotations-method-annotation",
            description = "Controls which @Generated annotation should be used for methods.",
            defaultValue = AnnotationConfiguration.DEFAULT_CLASS_OPTION_STRING)
    AnnotationClassOptions methodAnnotation;

    @CommandLine.Option(
            names = "--annotations-generator-name",
            description = "The name of the code generator.",
            defaultValue = AnnotationConfiguration.DEFAULT_GENERATOR_NAME)
    String generatorName;

    @CommandLine.Option(
            names = "--annotations-class-members",
            description = "Controls which annotation members should be used for classes.",
            defaultValue = AnnotationConfiguration.DEFAULT_MEMBER_OPTION_STRING)
    AnnotationMemberOptions classMembers;

    @CommandLine.Option(
            names = "--annotations-method-members",
            description = "Controls which annotation members should be used for methods.",
            defaultValue = AnnotationConfiguration.DEFAULT_MEMBER_OPTION_STRING)
    AnnotationMemberOptions methodMembers;

    @CommandLine.Option(
            names = "--annotations-field-members",
            description = "Controls which annotation members should be used for fields.",
            defaultValue = AnnotationConfiguration.DEFAULT_MEMBER_OPTION_STRING)
    AnnotationMemberOptions fieldMembers;

    public AnnotationConfiguration asConfiguration() {
        return AnnotationConfiguration.builder()
                .setClassComment(classComment)
                .setFieldComment(fieldComment)
                .setMethodComment(methodComment)
                .setClassAnnotation(classAnnotation)
                .setFieldAnnotation(fieldAnnotation)
                .setMethodAnnotation(methodAnnotation)
                .setClassMembers(classMembers)
                .setMethodMembers(methodMembers)
                .setFieldMembers(fieldMembers)
                .setGeneratorName(generatorName)
                .build();
    }

}
