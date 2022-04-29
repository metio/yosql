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
import wtf.metio.yosql.dao.fluentjdbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the FluentJDBC based DAO implementation.
 */
@Module
public class FluentJdbcDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @FluentJDBC FieldsGenerator fields,
            final @FluentJDBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.EBEAN);
    }

    @FluentJDBC
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @FluentJDBC ConstructorGenerator constructor,
            final @FluentJDBC BlockingMethodGenerator blockingMethods,
            final @FluentJDBC BatchMethodGenerator batchMethods,
            final @FluentJDBC Java8StreamMethodGenerator streamMethods,
            final @FluentJDBC RxJavaMethodGenerator rxjavaMethods,
            final @FluentJDBC ReactorMethodGenerator reactorMethods,
            final @FluentJDBC MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @FluentJDBC
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new FluentJdbcConstructorGenerator(blocks, methods);
    }

    @FluentJDBC
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new FluentJdbcFieldsGenerator(fields, logging, javadoc);
    }

    @FluentJDBC
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new FluentJdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @FluentJDBC
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new FluentJdbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @FluentJDBC
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final ControlFlows controlFlows,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new FluentJdbcRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @FluentJDBC
    @Provides
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new FluentJdbcMutinyMethodGenerator();
    }

    @FluentJDBC
    @Provides
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new FluentJdbcReactorMethodGenerator();
    }

    @FluentJDBC
    @Provides
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new FluentJdbcBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
