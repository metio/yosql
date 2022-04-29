/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.codegen.blocks;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

@Module
public class DefaultGenericBlocksModule {

    @Provides
    public Javadoc provideJavadoc(final RuntimeConfiguration runtimeConfiguration, final IMessageConveyor messages) {
        return new DefaultJavadoc(runtimeConfiguration.files(), messages);
    }

    @Provides
    public AnnotationGenerator provideAnnotationGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultAnnotationGenerator(runtimeConfiguration.annotations(), runtimeConfiguration.api());
    }

    @Provides
    public Classes provideClasses(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultClasses(runtimeConfiguration.java());
    }

    @Provides
    public ControlFlows provideControlFlows(
            final Variables variables,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultControlFlows(variables, runtimeConfiguration.names());
    }

    @Provides
    public Fields provideFields(final AnnotationGenerator annotations, final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultFields(annotations, runtimeConfiguration.java());
    }

    @Provides
    public GenericBlocks provideGenericBlocks() {
        return new DefaultGenericBlocks();
    }

    @Provides
    public Methods provideMethods(
            final AnnotationGenerator annotations,
            final Javadoc javadoc,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultMethods(annotations, javadoc, runtimeConfiguration.java());
    }

    @Provides
    public Parameters provideParameters(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultParameters(runtimeConfiguration.names(), runtimeConfiguration.java());
    }

    @Provides
    public Variables provideVariables(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultVariables(runtimeConfiguration.java());
    }

}
