/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.dao;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.dao.spring.jdbc.*;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the Spring-JDBC based DAO implementation.
 */
@Module
public class SpringJdbcDaoModule {

    @Provides
    @SpringJDBC
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
            final @SpringJDBC BatchMethodGenerator batchMethods,
            final @SpringJDBC Java8StreamMethodGenerator streamMethods,
            final @SpringJDBC RxJavaMethodGenerator rxjavaMethods,
            final @SpringJDBC StandardMethodGenerator standardMethods,
            final @SpringJDBC ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor,
                standardMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @Provides
    @SpringJDBC
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new SpringJdbcConstructorGenerator(blocks, methods);
    }

    @Provides
    @SpringJDBC
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new SpringJdbcFieldsGenerator(fields, logging, javadoc);
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
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringJDBC
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @Provides
    @SpringJDBC
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new SpringJdbcStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
