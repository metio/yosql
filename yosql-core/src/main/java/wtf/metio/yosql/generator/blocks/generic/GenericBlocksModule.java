/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.configuration.VariableConfiguration;

@Module
public class GenericBlocksModule {

    @Provides
    AnnotationGenerator annotationGenerator(final RuntimeConfiguration runtime, final Translator translator) {
        return new DefaultAnnotationGenerator(runtime.annotations(), translator);
    }

    @Provides
    Classes classes() {
        return new DefaultClasses();
    }

    @Provides
    ControlFlows controlFlows(
            final Variables variables,
            final Names names) {
        return new DefaultControlFlows(variables, names);
    }

    @Provides
    Fields fields(final AnnotationGenerator annotations) {
        return new DefaultFields(annotations);
    }

    @Provides
    GenericBlocks genericBlocks() {
        return new DefaultGenericBlocks();
    }

    @Provides
    Methods methods(final AnnotationGenerator annotations, final Javadoc javadoc) {
        return new DefaultMethods(annotations, javadoc);
    }

    @Provides
    Names names() {
        return new DefaultNames();
    }

    @Provides
    Parameters parameters(final Names names) {
        return new DefaultParameters(names);
    }

    @Provides
    Variables variables(final VariableConfiguration options) {
        return new DefaultVariables(options);
    }

}
