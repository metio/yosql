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
import wtf.metio.yosql.dao.spring.data.jpa.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the Spring-Data-JPA based DAO implementation.
 */
@Module
public class SpringDataJpaDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @SpringDataJPA FieldsGenerator fields,
            final @SpringDataJPA MethodsGenerator methods) {
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
    @SpringDataJPA
    MethodsGenerator provideMethodsGenerator(
            final @SpringDataJPA ConstructorGenerator constructor,
            final @SpringDataJPA BlockingMethodGenerator blockingMethods,
            final @SpringDataJPA BatchMethodGenerator batchMethods,
            final @SpringDataJPA Java8StreamMethodGenerator streamMethods,
            final @SpringDataJPA RxJavaMethodGenerator rxjavaMethods,
            final @SpringDataJPA ReactorMethodGenerator reactorMethods,
            final @SpringDataJPA MutinyMethodGenerator mutinyMethods) {
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
    @SpringDataJPA
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new SpringDataJpaConstructorGenerator(blocks, methods);
    }

    @Provides
    @SpringDataJPA
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SpringDataJpaFieldsGenerator(fields, logging, javadoc);
    }

    @Provides
    @SpringDataJPA
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJpaBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJPA
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJpaJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJPA
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJpaRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJPA
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new SpringDataJpaMutinyMethodGenerator();
    }

    @Provides
    @SpringDataJPA
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new SpringDataJpaReactorMethodGenerator();
    }

    @Provides
    @SpringDataJPA
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJpaBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
