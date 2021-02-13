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
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

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
    Classes provideClasses() {
        return new DefaultClasses();
    }

    @Provides
    ControlFlows provideControlFlows(
            final Variables variables,
            final Names names) {
        return new DefaultControlFlows(variables, names);
    }

    @Provides
    Fields provideFields(final AnnotationGenerator annotations) {
        return new DefaultFields(annotations);
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
        return new DefaultParameters(names);
    }

    @Provides
    Variables provideVariables(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultVariables(runtimeConfiguration.java());
    }

}
