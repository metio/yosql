/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.blocks;

import ch.qos.cal10n.IMessageConveyor;
import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.orchestration.ExecutionErrors;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

@Module
public class DefaultBlocksModule {

    @Provides
    @Singleton
    Javadoc provideJavadoc(final RuntimeConfiguration runtimeConfiguration, final IMessageConveyor messages) {
        return new DefaultJavadoc(runtimeConfiguration.files(), messages);
    }

    @Provides
    @Singleton
    Annotations provideAnnotationGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final IMessageConveyor messages,
            final ExecutionErrors errors) {
        return new DefaultAnnotations(runtimeConfiguration.annotations(), errors, messages);
    }

    @Provides
    @Singleton
    Classes provideClasses(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultClasses(runtimeConfiguration.java());
    }

    @Provides
    @Singleton
    ControlFlows provideControlFlows(
            final RuntimeConfiguration runtimeConfiguration,
            final Variables variables) {
        return new DefaultControlFlows(variables, runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    Fields provideFields(
            final RuntimeConfiguration runtimeConfiguration,
            final Annotations annotations) {
        return new DefaultFields(annotations, runtimeConfiguration.java(), runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    CodeBlocks provideCodeBlocks() {
        return new DefaultCodeBlocks();
    }

    @Provides
    @Singleton
    Methods provideMethods(
            final RuntimeConfiguration runtimeConfiguration,
            final Annotations annotations,
            final Javadoc javadoc) {
        return new DefaultMethods(annotations, javadoc, runtimeConfiguration.java());
    }

    @Provides
    @Singleton
    Parameters provideParameters(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultParameters(runtimeConfiguration.java());
    }

    @Provides
    @Singleton
    Variables provideVariables(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultVariables(runtimeConfiguration.java());
    }

}
