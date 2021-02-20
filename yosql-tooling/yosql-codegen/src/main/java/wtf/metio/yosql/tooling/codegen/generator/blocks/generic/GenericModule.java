/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.tooling.codegen.generator.api.AnnotationGenerator;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;

@Module
public final class GenericModule {

    @Provides
    Javadoc provideJavadoc() {
        return new DefaultJavadoc();
    }

    @Provides
    AnnotationGenerator provideAnnotationGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultAnnotationGenerator(runtimeConfiguration.annotations());
    }

    @Provides
    Classes provideClasses(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultClasses(runtimeConfiguration.java());
    }

    @Provides
    ControlFlows provideControlFlows(
            final Variables variables,
            final Names names) {
        return new DefaultControlFlows(variables, names);
    }

    @Provides
    Fields provideFields(final AnnotationGenerator annotations, final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultFields(annotations, runtimeConfiguration.java());
    }

    @Provides
    GenericBlocks provideGenericBlocks() {
        return new DefaultGenericBlocks();
    }

    @Provides
    Methods provideMethods(final AnnotationGenerator annotations, final Javadoc javadoc, final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethods(annotations, javadoc, runtimeConfiguration.java());
    }

    @Provides
    Names provideNames() {
        return new DefaultNames();
    }

    @Provides
    Parameters provideParameters(final Names names) {
        return new DefaultParameters(names, RuntimeConfiguration.usingDefaults());
    }

    @Provides
    Variables provideVariables(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultVariables(runtimeConfiguration.java());
    }

}
