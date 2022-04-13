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
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.dao.spring.data.jdbc.*;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the Spring-Data-JDBC based DAO implementation.
 */
@Module
public class SpringDataJdbcDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @SpringDataJDBC FieldsGenerator fields,
            final @SpringDataJDBC MethodsGenerator methods) {
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
    @SpringDataJDBC
    MethodsGenerator provideMethodsGenerator(
            final @SpringDataJDBC BatchMethodGenerator batchMethods,
            final @SpringDataJDBC Java8StreamMethodGenerator streamMethods,
            final @SpringDataJDBC RxJavaMethodGenerator rxjavaMethods,
            final @SpringDataJDBC StandardMethodGenerator standardMethods,
            final @SpringDataJDBC ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor, standardMethods, batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @Provides
    @SpringDataJDBC
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new SpringDataJdbcConstructorGenerator(blocks, methods);
    }

    @Provides
    @SpringDataJDBC
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SpringDataJdbcFieldsGenerator(fields, logging, javadoc);
    }

    @Provides
    @SpringDataJDBC
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJDBC
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJdbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJDBC
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJdbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringDataJDBC
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringDataJdbcStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
