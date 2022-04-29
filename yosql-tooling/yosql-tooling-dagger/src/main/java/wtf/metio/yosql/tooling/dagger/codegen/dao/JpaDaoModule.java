/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.dao;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.dao.jpa.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the JPA based DAO implementation.
 */
@Module
public class JpaDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @JPA FieldsGenerator fields,
            final @JPA MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.JPA);
    }

    @JPA
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @JPA ConstructorGenerator constructor,
            final @JPA BlockingMethodGenerator blockingMethods,
            final @JPA BatchMethodGenerator batchMethods,
            final @JPA Java8StreamMethodGenerator streamMethods,
            final @JPA RxJavaMethodGenerator rxjavaMethods,
            final @JPA ReactorMethodGenerator reactorMethods,
            final @JPA MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @JPA
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new JpaConstructorGenerator(blocks, methods);
    }

    @JPA
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new JpaFieldsGenerator(fields, logging, javadoc);
    }

    @JPA
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JpaBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @JPA
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JpaJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @JPA
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JpaRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @JPA
    @Provides
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new JpaMutinyMethodGenerator();
    }

    @JPA
    @Provides
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new JpaReactorMethodGenerator();
    }

    @JPA
    @Provides
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JpaBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
