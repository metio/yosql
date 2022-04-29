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
import wtf.metio.yosql.dao.pyranid.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the Pyranid based DAO implementation.
 */
@Module
public class PyranidDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @Pyranid FieldsGenerator fields,
            final @Pyranid MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.EBEAN);
    }

    @Pyranid
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @Pyranid ConstructorGenerator constructor,
            final @Pyranid BlockingMethodGenerator blockingMethods,
            final @Pyranid BatchMethodGenerator batchMethods,
            final @Pyranid Java8StreamMethodGenerator streamMethods,
            final @Pyranid RxJavaMethodGenerator rxjavaMethods,
            final @Pyranid ReactorMethodGenerator reactorMethods,
            final @Pyranid MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @Pyranid
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new PyranidConstructorGenerator(blocks, methods);
    }

    @Pyranid
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new PyranidFieldsGenerator(fields, logging, javadoc);
    }

    @Pyranid
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new PyranidBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Pyranid
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new PyranidJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Pyranid
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new PyranidRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Pyranid
    @Provides
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new PyranidMutinyMethodGenerator();
    }

    @Pyranid
    @Provides
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new PyranidReactorMethodGenerator();
    }

    @Pyranid
    @Provides
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new PyranidBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
