/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.testing.codegen;

import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.testing.configs.Annotations;
import wtf.metio.yosql.testing.configs.Files;
import wtf.metio.yosql.testing.configs.Java;

/**
 * Object mother for types in the codegen package.
 */
public final class Blocks {

    public static Names names() {
        return new DefaultNames();
    }

    public static Classes classes() {
        return new DefaultClasses(Java.defaults());
    }

    public static Parameters parameters() {
        return new DefaultParameters(names(), Java.defaults());
    }

    public static Methods methods() {
        return new DefaultMethods(annotationGenerator(), javadoc(), Java.defaults());
    }

    public static Fields fields() {
        return fields(Java.defaults());
    }

    public static Fields fields(final JavaConfiguration java) {
        return new DefaultFields(annotationGenerator(), java);
    }

    public static Variables variables() {
        return new DefaultVariables(Java.defaults());
    }

    public static GenericBlocks genericBlocks() {
        return new DefaultGenericBlocks();
    }

    public static ControlFlows controlFlows() {
        return new DefaultControlFlows(variables(), names());
    }

    public static AnnotationGenerator annotationGenerator() {
        return new DefaultAnnotationGenerator(Annotations.defaults());
    }

    public static Javadoc javadoc() {
        return javadoc(Files.maven());
    }

    public static Javadoc javadoc(final FilesConfiguration files) {
        return new DefaultJavadoc(files);
    }

    private Blocks() {
        // factory class
    }

}
