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
import wtf.metio.yosql.codegen.blocks.GenericMethodsGenerator;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.dao.ebean.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * Dagger module for the JPA based DAO implementation.
 */
@Module
public class EBeanDaoModule {

    @IntoSet
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @EBean FieldsGenerator fields,
            final @EBean MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.EBEAN);
    }

    @EBean
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @EBean BatchMethodGenerator batchMethods,
            final @EBean Java8StreamMethodGenerator streamMethods,
            final @EBean RxJavaMethodGenerator rxjavaMethods,
            final @EBean StandardMethodGenerator standardMethods,
            final @EBean ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor, standardMethods, batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @EBean
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods) {
        return new EBeanConstructorGenerator(blocks, methods);
    }

    @EBean
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc) {
        return new EBeanFieldsGenerator(fields, logging, javadoc);
    }

    @EBean
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new EBeanBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging);
    }

    @EBean
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new EBeanJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging);
    }

    @EBean
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new EBeanRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging);
    }

    @EBean
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging) {
        return new EBeanStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging);
    }

}
