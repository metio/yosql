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
import wtf.metio.yosql.dao.r2dbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for the R2DBC based DAO implementation.
 */
@Module
public class R2dbcDaoModule {

    @IntoSet
    @Provides
    @Singleton
    RepositoryGenerator provideRepositoryGenerator(
            @Generator final LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            @R2DBC final FieldsGenerator fields,
            @R2DBC final MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.R2DBC);
    }

    @R2DBC
    @Provides
    @Singleton
    MethodsGenerator provideMethodsGenerator(
            @R2DBC final ConstructorGenerator constructor,
            @R2DBC final BlockingMethodGenerator blockingMethods,
            @R2DBC final BatchMethodGenerator batchMethods,
            @R2DBC final Java8StreamMethodGenerator streamMethods,
            @R2DBC final RxJavaMethodGenerator rxjavaMethods,
            @R2DBC final ReactorMethodGenerator reactorMethods,
            @R2DBC final MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @R2DBC
    @Provides
    @Singleton
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new R2dbcConstructorGenerator(blocks, methods);
    }

    @R2DBC
    @Provides
    @Singleton
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new R2dbcFieldsGenerator(fields, logging, javadoc);
    }

    @R2DBC
    @Provides
    @Singleton
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2dbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @R2DBC
    @Provides
    @Singleton
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2dbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @R2DBC
    @Provides
    @Singleton
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2dbcRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @R2DBC
    @Provides
    @Singleton
    MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new R2dbcMutinyMethodGenerator();
    }

    @R2DBC
    @Provides
    @Singleton
    ReactorMethodGenerator provideReactorMethodGenerator() {
        return new R2dbcReactorMethodGenerator();
    }

    @R2DBC
    @Provides
    @Singleton
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2DbcBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
