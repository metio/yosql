/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.blocks;

import ch.qos.cal10n.MessageConveyor;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.internals.testing.configs.AnnotationsConfigurations;
import wtf.metio.yosql.internals.testing.configs.FilesConfigurations;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

/**
 * Object mother for types in the codegen package.
 */
public final class BlocksObjectMother {

    public static Classes classes(final JavaConfiguration java) {
        return new DefaultClasses(java);
    }

    public static Parameters parameters() {
        return parameters(JavaConfigurations.defaults());
    }

    public static Parameters parameters(final JavaConfiguration java) {
        return new DefaultParameters(java);
    }

    public static Methods methods(final JavaConfiguration java) {
        return new DefaultMethods(annotationGenerator(), javadoc(), java);
    }

    public static Fields fields() {
        return fields(JavaConfigurations.defaults());
    }

    public static Fields fields(final JavaConfiguration java) {
        return new DefaultFields(annotationGenerator(), java, NamesConfigurations.defaults());
    }

    public static Variables variables(final JavaConfiguration java) {
        return new DefaultVariables(java);
    }

    public static CodeBlocks codeBlocks() {
        return new DefaultCodeBlocks();
    }

    public static ControlFlows controlFlows(final JavaConfiguration java) {
        return new DefaultControlFlows(variables(java), NamesConfigurations.defaults());
    }

    public static Annotations annotationGenerator() {
        return new DefaultAnnotations(AnnotationsConfigurations.defaults());
    }

    public static Javadoc javadoc() {
        return javadoc(FilesConfigurations.maven());
    }

    public static Javadoc javadoc(final FilesConfiguration files) {
        return new DefaultJavadoc(files, new MessageConveyor(SupportedLocales.ENGLISH));
    }

    private BlocksObjectMother() {
        // factory class
    }

}
