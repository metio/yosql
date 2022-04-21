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
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the R2DBC based DAO implementation.
 */
@Module
public class R2dbcDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @R2DBC FieldsGenerator fields,
            final @R2DBC MethodsGenerator methods) {
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
    MethodsGenerator provideMethodsGenerator(
            final @R2DBC ConstructorGenerator constructor,
            final @R2DBC BlockingMethodGenerator blockingMethods,
            final @R2DBC BatchMethodGenerator batchMethods,
            final @R2DBC Java8StreamMethodGenerator streamMethods,
            final @R2DBC RxJavaMethodGenerator rxjavaMethods,
            final @R2DBC ReactorMethodGenerator reactorMethods,
            final @R2DBC MutinyMethodGenerator mutinyMethods) {
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
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new R2dbcConstructorGenerator(blocks, methods);
    }

    @R2DBC
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new R2dbcFieldsGenerator(fields, logging, javadoc);
    }

    @R2DBC
    @Provides
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
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2dbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @R2DBC
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new R2dbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @R2DBC
    @Provides
    public MutinyMethodGenerator provideMutinyMethodGenerator() {
        return new R2dbcMutinyMethodGenerator();
    }

    @R2DBC
    @Provides
    public ReactorMethodGenerator provideReactorMethodGenerator() {
        return new R2dbcReactorMethodGenerator();
    }

    @R2DBC
    @Provides
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
