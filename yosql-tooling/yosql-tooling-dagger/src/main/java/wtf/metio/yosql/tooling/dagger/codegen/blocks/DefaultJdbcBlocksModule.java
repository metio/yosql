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
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Names;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.codegen.api.Variables;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.dao.jdbc.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

@Module
public class DefaultJdbcBlocksModule {

    @Provides
    public JdbcTransformer provideJdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    @Provides
    public JdbcFields provideJdbcFields(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcFields(runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcParameters provideJdbcParameters(
            final RuntimeConfiguration runtimeConfiguration,
            final Parameters parameters) {
        return new DefaultJdbcParameters(parameters, runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcDataSourceMethods provideDataSource(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDataSourceMethods(runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcConnectionMethods provideConnection(
            final RuntimeConfiguration runtimeConfiguration,
            final Names names) {
        return new DefaultJdbcConnectionMethods(names, runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcResultSetMethods provideResultSet(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMethods(runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcResultSetMetaDataMethods provideMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcResultSetMetaDataMethods(runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcStatementMethods provideStatement(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcStatementMethods(runtimeConfiguration.jdbc());
    }

    @Provides
    public JdbcMethods.JdbcDatabaseMetaDataMethods provideDatabaseMetaData(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcDatabaseMetaDataMethods(runtimeConfiguration.jdbc());
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
            final Names names,
            final Variables variables,
            final JdbcFields jdbcFields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging) {
        return new DefaultJdbcBlocks(
                runtimeConfiguration,
                blocks,
                controlFlows,
                names,
                variables,
                runtimeConfiguration.jdbc(),
                jdbcFields,
                jdbcMethods,
                logging);
    }

}
