/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
        return new DefaultClasses();
    }

    @Provides
    @Singleton
    ControlFlows provideControlFlows(
            final RuntimeConfiguration runtimeConfiguration,
            final Variables variables) {
        return new DefaultControlFlows(variables);
    }

    @Provides
    @Singleton
    Fields provideFields(
            final RuntimeConfiguration runtimeConfiguration,
            final Annotations annotations) {
        return new DefaultFields(annotations);
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
        return new DefaultMethods(annotations, javadoc);
    }

    @Provides
    @Singleton
    Parameters provideParameters(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultParameters();
    }

    @Provides
    @Singleton
    Variables provideVariables(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultVariables();
    }

}
