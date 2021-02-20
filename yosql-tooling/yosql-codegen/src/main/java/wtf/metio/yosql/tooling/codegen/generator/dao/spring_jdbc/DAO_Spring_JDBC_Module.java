/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc;

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
 * Dagger module for the Spring-JDBC based DAO implementation.
 */
@Module
public class DAO_Spring_JDBC_Module {

    @Provides
    @Spring_JDBC
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @Spring_JDBC FieldsGenerator fields,
            final @Spring_JDBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(logger, annotations, classes, javadoc, fields, methods);
    }

    @Provides
    @Spring_JDBC
    MethodsGenerator provideMethodsGenerator(
            final @Spring_JDBC BatchMethodGenerator batchMethods,
            final @Spring_JDBC Java8StreamMethodGenerator streamMethods,
            final @Spring_JDBC RxJavaMethodGenerator rxjavaMethods,
            final @Spring_JDBC StandardMethodGenerator standardMethods,
            final @Spring_JDBC ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                batchMethods,
                streamMethods,
                rxjavaMethods,
                standardMethods,
                constructor);
    }

    @Provides
    @Spring_JDBC
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final JdbcNames jdbcNames,
            final JdbcParameters jdbcParameters) {
        return new SpringJdbcConstructorGenerator(blocks, methods, jdbcNames, jdbcParameters);
    }

    @Provides
    @Spring_JDBC
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final JdbcNames jdbcNames,
            final Javadoc javadoc) {
        return new SpringJdbcFieldsGenerator(fields, logging, jdbcFields, jdbcNames, javadoc);
    }

    @Provides
    @Spring_JDBC
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        return new SpringJdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging,
                jdbc,
                transformer);
    }

    @Provides
    @Spring_JDBC
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer) {
        return new SpringJdbcJava8StreamMethodGenerator(
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

    @Provides
    @Spring_JDBC
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        return new SpringJdbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                methods,
                parameters,
                logging,
                jdbcBlocks);
    }

    @Provides
    @Spring_JDBC
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        return new SpringJdbcStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbc,
                jdbcTransformer);
    }

}
