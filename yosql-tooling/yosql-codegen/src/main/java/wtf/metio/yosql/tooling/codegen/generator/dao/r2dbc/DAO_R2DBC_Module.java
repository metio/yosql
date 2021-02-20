/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.dao.r2dbc;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.tooling.codegen.generator.api.*;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.*;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.*;
import wtf.metio.yosql.tooling.codegen.generator.dao.generic.GenericMethodsGenerator;
import wtf.metio.yosql.tooling.codegen.generator.dao.generic.GenericRepositoryGenerator;
import wtf.metio.yosql.tooling.codegen.model.annotations.Delegating;
import wtf.metio.yosql.tooling.codegen.model.annotations.Generator;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;

/**
 * Dagger module for the R2DBC based DAO implementation.
 */
@Module
public class DAO_R2DBC_Module {

    @R2DBC
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @R2DBC FieldsGenerator fields,
            final @R2DBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(logger, annotations, classes, javadoc, fields, methods);
    }

    @R2DBC
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @R2DBC BatchMethodGenerator batchMethods,
            final @R2DBC Java8StreamMethodGenerator streamMethods,
            final @R2DBC RxJavaMethodGenerator rxjavaMethods,
            final @R2DBC StandardMethodGenerator standardMethods,
            final @R2DBC ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                batchMethods,
                streamMethods,
                rxjavaMethods,
                standardMethods,
                constructor);
    }

    @R2DBC
    @Provides
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final JdbcNames jdbcNames,
            final JdbcParameters jdbcParameters) {
        return new R2dbcConstructorGenerator(blocks, methods, jdbcNames, jdbcParameters);
    }

    @R2DBC
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final JdbcNames jdbcNames,
            final Javadoc javadoc) {
        return new R2dbcFieldsGenerator(fields, logging, jdbcFields, jdbcNames, javadoc);
    }

    @R2DBC
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        return new R2dbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging,
                jdbc,
                transformer);
    }

    @R2DBC
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer) {
        return new R2dbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                jdbcTransformer
        );
    }

    @R2DBC
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        return new R2dbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging,
                jdbcBlocks);
    }

    @R2DBC
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        return new R2dbcStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbc,
                jdbcTransformer);
    }

}
