/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
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
import wtf.metio.yosql.codegen.blocks.GenericMethodsGenerator;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.dao.jdbi.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the JPA based DAO implementation.
 */
@Module
public class JdbiDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @JDBI FieldsGenerator fields,
            final @JDBI MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.JOOQ);
    }

    @JDBI
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @JDBI BatchMethodGenerator batchMethods,
            final @JDBI Java8StreamMethodGenerator streamMethods,
            final @JDBI RxJavaMethodGenerator rxjavaMethods,
            final @JDBI StandardMethodGenerator standardMethods,
            final @JDBI ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor, standardMethods, batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @JDBI
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new JdbiConstructorGenerator(blocks, methods);
    }

    @JDBI
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new JdbiFieldsGenerator(fields, logging, javadoc);
    }

    @JDBI
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JdbiBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @JDBI
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JdbiJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @JDBI
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JdbiRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @JDBI
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JdbiStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}