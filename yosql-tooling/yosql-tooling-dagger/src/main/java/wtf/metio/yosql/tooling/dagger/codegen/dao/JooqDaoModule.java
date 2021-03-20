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
import wtf.metio.yosql.dao.jooq.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the JPA based DAO implementation.
 */
@Module
public class JooqDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @Jooq FieldsGenerator fields,
            final @Jooq MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.JOOQ);
    }

    @Jooq
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @Jooq BatchMethodGenerator batchMethods,
            final @Jooq Java8StreamMethodGenerator streamMethods,
            final @Jooq RxJavaMethodGenerator rxjavaMethods,
            final @Jooq StandardMethodGenerator standardMethods,
            final @Jooq ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor, standardMethods, batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @Jooq
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new JooqConstructorGenerator(blocks, methods);
    }

    @Jooq
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new JooqFieldsGenerator(fields, logging, javadoc);
    }

    @Jooq
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JooqBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @Jooq
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JooqJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @Jooq
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JooqRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @Jooq
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new JooqStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}