/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.dao.jdbc;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.generator.api.*;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcFields;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.generator.dao.generic.GenericMethodsGenerator;
import wtf.metio.yosql.generator.dao.generic.GenericRepositoryGenerator;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.annotations.Generator;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

/**
 * Dagger module for the JDBC based DAO implementation.
 */
@Module
public class DaoJdbcModule {

    @JDBC
    @Provides
    RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @JDBC FieldsGenerator fields,
            final @JDBC MethodsGenerator methods) {
        return new GenericRepositoryGenerator(logger, annotations, classes, javadoc, fields, methods);
    }

    @JDBC
    @Provides
    MethodsGenerator provideMethodsGenerator(
            final @JDBC BatchMethodGenerator batchMethods,
            final @JDBC Java8StreamMethodGenerator streamMethods,
            final @JDBC RxJavaMethodGenerator rxjavaMethods,
            final @JDBC StandardMethodGenerator standardMethods,
            final GenericBlocks blocks,
            final AnnotationGenerator annotations,
            final Methods methods,
            final JdbcNamesConfiguration jdbcNames,
            final JdbcParameters jdbcParameters) {
        return new GenericMethodsGenerator(
                batchMethods,
                streamMethods,
                rxjavaMethods,
                standardMethods,
                blocks,
                annotations,
                methods,
                jdbcNames,
                jdbcParameters);
    }

    @JDBC
    @Provides
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final JdbcNamesConfiguration jdbcNames) {
        return new JdbcFieldsGenerator(fields, logging, jdbcFields, jdbcNames);
    }

    @JDBC
    @Provides
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final AnnotationGenerator annotations,
            final Javadoc javadoc,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        return new JdbcBatchMethodGenerator(
                controlFlow,
                annotations,
                javadoc,
                methods,
                parameters,
                logging,
                jdbc,
                transformer);
    }

    @JDBC
    @Provides
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Javadoc javadoc,
            final AnnotationGenerator annotations,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer,
            final JdbcNamesConfiguration jdbcNames) {
        return new JdbcJava8StreamMethodGenerator(
                blocks,
                controlFlow,
                names,
                javadoc,
                annotations,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                jdbcTransformer,
                jdbcNames);
    }

    @JDBC
    @Provides
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final AnnotationGenerator annotations,
            final Javadoc javadoc,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        return new JdbcRxJavaMethodGenerator(
                configuration,
                controlFlows,
                names,
                annotations,
                javadoc,
                methods,
                parameters,
                logging,
                jdbcBlocks);
    }

    @JDBC
    @Provides
    StandardMethodGenerator provideStandardMethodGenerator(
            final ControlFlows controlFlows,
            final AnnotationGenerator annotations,
            final Methods methods,
            final Javadoc javadoc,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        return new JdbcStandardMethodGenerator(
                controlFlows,
                annotations,
                methods,
                javadoc,
                parameters,
                logging,
                jdbc,
                jdbcTransformer);
    }

}