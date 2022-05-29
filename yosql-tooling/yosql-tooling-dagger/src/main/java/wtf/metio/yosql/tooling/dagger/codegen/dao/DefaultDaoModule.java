/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.dagger.codegen.dao;

import dagger.Module;
import dagger.Provides;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.blocks.*;
import wtf.metio.yosql.codegen.dao.*;
import wtf.metio.yosql.codegen.logging.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.annotations.Converter;
import wtf.metio.yosql.tooling.dagger.annotations.Delegating;
import wtf.metio.yosql.tooling.dagger.annotations.Generator;

import javax.inject.Singleton;

/**
 * Dagger module for the DAO APIs.
 */
@Module
public class DefaultDaoModule {

    @Provides
    @Singleton
    RepositoryGenerator provideRepositoryGenerator(
            @Generator final LocLogger logger,
            final Annotations annotations,
            final Classes classes,
            final Javadoc javadoc,
            final FieldsGenerator fields,
            final MethodsGenerator methods) {
        return new DefaultRepositoryGenerator(
                logger,
                annotations,
                classes,
                javadoc,
                fields,
                methods);
    }

    @Provides
    @Singleton
    MethodsGenerator provideMethodsGenerator(
            final ConstructorGenerator constructor,
            final ReadMethodGenerator readMethods,
            final WriteMethodGenerator writeMethods,
            final CallMethodGenerator callMethods) {
        return new DefaultMethodsGenerator(
                constructor,
                readMethods,
                writeMethods,
                callMethods);
    }

    @Provides
    @Singleton
    ConstructorGenerator provideConstructorGenerator(
            final CodeBlocks blocks,
            final Methods methods,
            final RuntimeConfiguration runtimeConfiguration,
            final JdbcParameters jdbcParameters) {
        return new DefaultConstructorGenerator(
                blocks,
                methods,
                runtimeConfiguration.names(),
                jdbcParameters,
                runtimeConfiguration.repositories(),
                runtimeConfiguration.converter());
    }

    @Provides
    @Singleton
    FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final Javadoc javadoc,
            final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultFieldsGenerator(
                runtimeConfiguration.converter(),
                runtimeConfiguration.names(),
                logging,
                javadoc,
                fields);
    }

    @Provides
    @Singleton
    ParameterGenerator provideParameterGenerator(final Parameters parameters) {
        return new DefaultParameterGenerator(parameters);
    }

    @Provides
    @Singleton
    ConverterGenerator provideConverterGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ToMapConverterGenerator mapConverterGenerator) {
        return new DefaultConverterGenerator(
                runtimeConfiguration.converter(),
                mapConverterGenerator);
    }

    @Provides
    @Singleton
    ReturnTypes provideReturnTypes(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultReturnTypes(runtimeConfiguration.converter());
    }

    @Provides
    @Singleton
    ReadMethodGenerator provideReadMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final ParameterGenerator parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final MethodExceptionHandler exceptions,
            final RuntimeConfiguration runtimeConfiguration,
            final ReturnTypes returnTypes) {
        return new DefaultReadMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                exceptions,
                runtimeConfiguration.converter(),
                returnTypes);
    }

    @Provides
    @Singleton
    WriteMethodGenerator provideWriteMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final ParameterGenerator parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final MethodExceptionHandler exceptions,
            final RuntimeConfiguration runtimeConfiguration,
            final ReturnTypes returnTypes) {
        return new DefaultWriteMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                exceptions,
                runtimeConfiguration.converter(),
                returnTypes);
    }

    @Provides
    @Singleton
    CallMethodGenerator provideCallMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final ParameterGenerator parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final MethodExceptionHandler exceptions,
            final RuntimeConfiguration runtimeConfiguration,
            final ReturnTypes returnTypes) {
        return new DefaultCallMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbcBlocks,
                exceptions,
                runtimeConfiguration.converter(),
                returnTypes);
    }

    @Provides
    @Singleton
    ToMapConverterGenerator provideToMapConverterGenerator(
            @Converter final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final Annotations annotations,
            final Classes classes,
            final Methods methods,
            final Variables variables,
            final ControlFlows controlFlows,
            final JdbcParameters jdbcParameters,
            final JdbcBlocks jdbcBlocks,
            final MethodExceptionHandler exceptions) {
        return new ToMapConverterGenerator(
                logger,
                runtimeConfiguration,
                annotations,
                classes,
                methods,
                variables,
                controlFlows,
                jdbcParameters,
                jdbcBlocks,
                exceptions);
    }

    @Provides
    @Singleton
    MethodExceptionHandler provideJdbcMethodExceptionHandler() {
        return new DefaultMethodExceptionHandler();
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
            final CodeBlocks blocks,
            final ControlFlows controlFlows,
            final Variables variables,
            final FieldsGenerator fields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging,
            final Parameters params,
            final Methods methods) {
        return new DefaultJdbcBlocks(
                runtimeConfiguration,
                blocks,
                controlFlows,
                variables,
                fields,
                jdbcMethods,
                logging,
                params,
                methods);
    }

}
