/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.dagger.codegen.blocks;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.codegen.annotations.Delegating;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.dao.jdbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

@Module
public class DefaultJdbcBlocksModule {

    @Provides
    public ConverterConfiguration provideConverterConfiguration(final RuntimeConfiguration runtimeConfiguration) {
        return runtimeConfiguration.converter();
    }

    @Provides
    public MethodExceptionHandler provideJdbcMethodExceptionHandler() {
        return new JdbcMethodExceptionHandler();
    }

    @Provides
    public JdbcParameters provideJdbcParameters(
            final RuntimeConfiguration runtimeConfiguration,
            final Parameters parameters) {
        return new DefaultJdbcParameters(parameters, runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcDataSourceMethods provideDataSource(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDataSourceMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcConnectionMethods provideConnection(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcConnectionMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcResultSetMethods provideResultSet(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcResultSetMetaDataMethods provideMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMetaDataMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcStatementMethods provideStatement(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcStatementMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods.JdbcDatabaseMetaDataMethods provideDatabaseMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDatabaseMetaDataMethods(runtimeConfiguration.names());
    }

    @Provides
    public JdbcMethods provideJdbcMethods(
            final JdbcMethods.JdbcDataSourceMethods dataSource,
            final JdbcMethods.JdbcConnectionMethods connection,
            final JdbcMethods.JdbcDatabaseMetaDataMethods databaseMetaData,
            final JdbcMethods.JdbcResultSetMethods resultSet,
            final JdbcMethods.JdbcResultSetMetaDataMethods resultSetMetaData,
            final JdbcMethods.JdbcStatementMethods statement) {
        return new DefaultJdbcMethods(dataSource, connection, databaseMetaData, resultSet, resultSetMetaData, statement);
    }

    @Provides
    public JdbcBlocks provideJdbcBlocks(
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
