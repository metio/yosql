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
import wtf.metio.yosql.dao.sansorm.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the SansOrm based DAO implementation.
 */
@Module
public class SansOrmDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @SansOrm FieldsGenerator fields,
            final @SansOrm MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.EBEAN);
    }

    @SansOrm
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @SansOrm ConstructorGenerator constructor,
            final @SansOrm BlockingMethodGenerator blockingMethods,
            final @SansOrm BatchMethodGenerator batchMethods,
            final @SansOrm Java8StreamMethodGenerator streamMethods,
            final @SansOrm RxJavaMethodGenerator rxjavaMethods,
            final @SansOrm ReactorMethodGenerator reactorMethods,
            final @SansOrm MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @SansOrm
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new SansOrmConstructorGenerator(blocks, methods);
    }

    @SansOrm
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SansOrmFieldsGenerator(fields, logging, javadoc);
    }

    @SansOrm
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SansOrmBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @SansOrm
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SansOrmJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @SansOrm
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SansOrmRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @SansOrm
    @Provides
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new SansOrmMutinyMethodGenerator();
    }

    @SansOrm
    @Provides
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new SansOrmReactorMethodGenerator();
    }

    @SansOrm
    @Provides
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SansOrmBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
