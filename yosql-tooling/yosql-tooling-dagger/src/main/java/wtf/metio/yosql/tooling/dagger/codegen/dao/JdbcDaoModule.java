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
import wtf.metio.yosql.dao.jdbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

import javax.inject.Singleton;

/**
 * Dagger module for the JDBC based DAO implementation.
 */
@Module
public class JdbcDaoModule {

    @IntoSet
    @Provides
    @Singleton
    RepositoryGenerator provideRepositoryGenerator(
            @Generator final LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            @JDBC final FieldsGenerator fields,
            @JDBC final MethodsGenerator methods) {
        return new GenericRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods,
                PersistenceApis.JDBC);
    }

    @JDBC
    @Provides
    @Singleton
    MethodsGenerator provideMethodsGenerator(
            @JDBC final ConstructorGenerator constructor,
            @JDBC final BlockingMethodGenerator blockingMethods,
            @JDBC final BatchMethodGenerator batchMethods,
            @JDBC final Java8StreamMethodGenerator streamMethods,
            @JDBC final RxJavaMethodGenerator rxjavaMethods,
            @JDBC final ReactorMethodGenerator reactorMethods,
            @JDBC final MutinyMethodGenerator mutinyMethods) {
        return new DelegatingMethodsGenerator(
                constructor,
                blockingMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods,
                reactorMethods,
                mutinyMethods);
    }

    @JDBC
    @Provides
    @Singleton
    ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final RuntimeConfiguration runtimeConfiguration,
            final JdbcParameters jdbcParameters) {
        return new JdbcConstructorGenerator(
                blocks,
                methods,
                runtimeConfiguration.names(),
                jdbcParameters,
                runtimeConfiguration.repositories(),
                runtimeConfiguration.converter());
    }

    @JDBC
    @Provides
    @Singleton
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc,
            final RuntimeConfiguration runtimeConfiguration) {
        return new JdbcFieldsGenerator(
                runtimeConfiguration.converter(),
                runtimeConfiguration.names(),
                logging,
                javadoc,
                fields);
    }

    @JDBC
    @Provides
    @Singleton
    BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            @JDBC final MethodExceptionHandler exceptions) {
        return new JdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging,
                jdbc,
                exceptions);
    }

    @JDBC
    @Provides
    @Singleton
    Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            @JDBC final MethodExceptionHandler methodExceptionHandler) {
        return new JdbcJava8StreamMethodGenerator(
                runtimeConfiguration.converter(),
                controlFlow,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                methodExceptionHandler
        );
    }

    @JDBC
    @Provides
    @Singleton
    RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            @JDBC final MethodExceptionHandler exceptions) {
        return new JdbcRxJavaMethodGenerator(
                runtimeConfiguration.converter(),
                methods,
                parameters,
                exceptions,
                controlFlows,
                logging,
                jdbcBlocks);
    }

    @JDBC
    @Provides
    @Singleton
    MutinyMethodGenerator provideMutinyMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            @JDBC final MethodExceptionHandler exceptions) {
        return new JdbcMutinyMethodGenerator(
                runtimeConfiguration.converter(),
                methods,
                parameters,
                exceptions,
                controlFlows,
                logging,
                jdbcBlocks);
    }

    @JDBC
    @Provides
    @Singleton
    ReactorMethodGenerator provideReactorMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            @JDBC final MethodExceptionHandler exceptions) {
        return new JdbcReactorMethodGenerator(
                runtimeConfiguration.converter(),
                methods,
                parameters,
                exceptions,
                controlFlows,
                logging,
                jdbcBlocks);
    }

    @JDBC
    @Provides
    @Singleton
    BlockingMethodGenerator provideBlockingMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            @JDBC final MethodExceptionHandler methodExceptionHandler) {
        return new JdbcBlockingMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbc,
                methodExceptionHandler,
                runtimeConfiguration.converter());
    }

    @IntoSet
    @Provides
    @Singleton
    ConverterGenerator provideConverterGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ToMapConverterGenerator mapConverterGenerator) {
        return new JdbcConverterGenerator(
                runtimeConfiguration.converter(),
                mapConverterGenerator);
    }

    @JDBC
    @Provides
    @Singleton
    MethodExceptionHandler provideJdbcMethodExceptionHandler() {
        return new JdbcMethodExceptionHandler();
    }

    @Provides
    @Singleton
    JdbcParameters provideJdbcParameters(
            final RuntimeConfiguration runtimeConfiguration,
            final Parameters parameters) {
        return new DefaultJdbcParameters(parameters, runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcDataSourceMethods provideDataSource(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDataSourceMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcConnectionMethods provideConnection(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcConnectionMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcResultSetMethods provideResultSet(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcResultSetMetaDataMethods provideMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMetaDataMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcStatementMethods provideStatement(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcStatementMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods.JdbcDatabaseMetaDataMethods provideDatabaseMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDatabaseMetaDataMethods(runtimeConfiguration.names());
    }

    @Provides
    @Singleton
    JdbcMethods provideJdbcMethods(
            final JdbcMethods.JdbcDataSourceMethods dataSource,
            final JdbcMethods.JdbcConnectionMethods connection,
            final JdbcMethods.JdbcDatabaseMetaDataMethods databaseMetaData,
            final JdbcMethods.JdbcResultSetMethods resultSet,
            final JdbcMethods.JdbcResultSetMetaDataMethods resultSetMetaData,
            final JdbcMethods.JdbcStatementMethods statement) {
        return new DefaultJdbcMethods(dataSource, connection, databaseMetaData, resultSet, resultSetMetaData, statement);
    }

    @Provides
    @Singleton
    JdbcBlocks provideJdbcBlocks(
            final RuntimeConfiguration runtimeConfiguration,
            final GenericBlocks blocks,
            final ControlFlows controlFlows,
            final Variables variables,
            final Fields fields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging) {
        return new DefaultJdbcBlocks(
                runtimeConfiguration,
                blocks,
                controlFlows,
                variables,
                fields,
                jdbcMethods,
                logging);
    }

}
