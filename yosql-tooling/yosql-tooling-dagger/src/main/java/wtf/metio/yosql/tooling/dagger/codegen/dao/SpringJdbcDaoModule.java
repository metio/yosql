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
import wtf.metio.yosql.dao.spring.jdbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.codegen.blocks.DefaultSpringJdbcBlocksModule;

/**
 * Dagger module for the Spring-JDBC based DAO implementation.
 */
@Module(includes = DefaultSpringJdbcBlocksModule.class)
public class SpringJdbcDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @SpringJDBC FieldsGenerator fields,
            final @SpringJDBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.SPRING_JDBC);
    }

    @Provides
    @SpringJDBC
    MethodsGenerator provideMethodsGenerator(
            final @SpringJDBC ConstructorGenerator constructor,
            final @SpringJDBC BlockingMethodGenerator blockingMethods,
            final @SpringJDBC BatchMethodGenerator batchMethods,
            final @SpringJDBC Java8StreamMethodGenerator streamMethods,
            final @SpringJDBC RxJavaMethodGenerator rxjavaMethods,
            final @SpringJDBC ReactorMethodGenerator reactorMethods,
            final @SpringJDBC MutinyMethodGenerator mutinyMethods) {
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
    @SpringJDBC
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final RuntimeConfiguration runtimeConfiguration,
            final SpringJdbcParameters parameters) {
        return new SpringJdbcConstructorGenerator(
                blocks,
                methods,
                runtimeConfiguration.repositories(),
                parameters,
                runtimeConfiguration.names());
    }

    @Provides
    @SpringJDBC
    FieldsGenerator provideFieldsGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SpringJdbcFieldsGenerator(
                fields,
                logging,
                javadoc,
                runtimeConfiguration.converter(),
                runtimeConfiguration.names());
    }

    @Provides
    @SpringJDBC
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringJDBC
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final RuntimeConfiguration runtimeConfiguration,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringJDBC
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcRxJavaMethodGenerator(
                runtimeConfiguration,
                controlFlows,
                runtimeConfiguration.names(),
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringJDBC
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new SpringJdbcMutinyMethodGenerator();
    }

    @Provides
    @SpringJDBC
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new SpringJdbcReactorMethodGenerator();
    }

    @Provides
    @SpringJDBC
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final SpringJdbcBlocks blocks) {
        return new SpringJdbcBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                blocks,
                runtimeConfiguration.converter());
    }

}
