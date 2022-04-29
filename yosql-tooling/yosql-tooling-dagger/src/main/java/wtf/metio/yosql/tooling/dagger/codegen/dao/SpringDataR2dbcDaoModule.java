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
import wtf.metio.yosql.dao.spring.data.r2dbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the Spring-Data-R2DBC based DAO implementation.
 */
@Module
public class SpringDataR2dbcDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @SpringDataR2DBC FieldsGenerator fields,
            final @SpringDataR2DBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.SPRING_DATA_JDBC);
    }

    @Provides
    @SpringDataR2DBC
    MethodsGenerator provideMethodsGenerator(
            final @SpringDataR2DBC ConstructorGenerator constructor,
            final @SpringDataR2DBC BlockingMethodGenerator blockingMethods,
            final @SpringDataR2DBC BatchMethodGenerator batchMethods,
            final @SpringDataR2DBC Java8StreamMethodGenerator streamMethods,
            final @SpringDataR2DBC RxJavaMethodGenerator rxjavaMethods,
            final @SpringDataR2DBC ReactorMethodGenerator reactorMethods,
            final @SpringDataR2DBC MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @Provides
    @SpringDataR2DBC
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new SpringDataR2dbcConstructorGenerator(blocks, methods);
    }

    @Provides
    @SpringDataR2DBC
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SpringDataR2dbcFieldsGenerator(fields, logging, javadoc);
    }

    @Provides
    @SpringDataR2DBC
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataR2dbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataR2DBC
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataR2dbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataR2DBC
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataR2dbcRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataR2DBC
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new SpringDataR2dbcMutinyMethodGenerator();
    }

    @Provides
    @SpringDataR2DBC
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new SpringDataR2dbcReactorMethodGenerator();
    }

    @Provides
    @SpringDataR2DBC
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataR2DbcBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
