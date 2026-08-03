/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.codegen.orchestration.OrchestrationObjectMother;
import wtf.metio.yosql.internals.testing.configs.AnnotationsConfigurations;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;
import wtf.metio.yosql.models.immutables.FilesConfiguration;

/**
 * Object mother for types in the codegen package.
 */
public final class BlocksObjectMother {

    public static Classes classes() {
        return new DefaultClasses();
    }

    public static Parameters parameters() {
        return new DefaultParameters();
    }

    public static Methods methods() {
        return new DefaultMethods(annotationGenerator(), javadoc());
    }

    public static Fields fields() {
        return new DefaultFields(annotationGenerator(), NamesConfigurations.defaults());
    }

    public static Variables variables() {
        return new DefaultVariables();
    }

    public static CodeBlocks codeBlocks() {
        return new DefaultCodeBlocks();
    }

    public static ControlFlows controlFlows() {
        return new DefaultControlFlows(variables(), NamesConfigurations.defaults());
    }

    public static Annotations annotationGenerator() {
        return new DefaultAnnotations(
                AnnotationsConfigurations.defaults(),
                OrchestrationObjectMother.executionErrors(),
                LoggingObjectMother.messages());
    }

    public static Javadoc javadoc() {
        return javadoc(FilesConfigurations.maven());
    }

    public static Javadoc javadoc(final FilesConfiguration files) {
        return new DefaultJavadoc(files, LoggingObjectMother.messages());
    }

    private BlocksObjectMother() {
        // factory class
    }

}
