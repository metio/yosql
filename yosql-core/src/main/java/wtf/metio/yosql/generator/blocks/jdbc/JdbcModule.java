/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import dagger.Module;
import dagger.Provides;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.generic.*;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

@Module
public class JdbcModule {

    @Provides
    public JdbcTransformer provideJdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    @Provides
    public JdbcFields provideJdbcFields(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcFields(runtimeConfiguration.jdbcFields());
    }

    @Provides
    public JdbcParameters provideJdbcParameters(
            final Parameters parameters,
            final JdbcNames names) {
        return new DefaultJdbcParameters(parameters, names);
    }

    @Provides
    public JdbcMethods.JdbcDataSourceMethods provideDataSource(final JdbcNames names) {
        return new DefaultJdbcDataSourceMethods(names);
    }

    @Provides
    public JdbcMethods.JdbcConnectionMethods provideConnection(
            final Names names,
            final JdbcNames jdbcNames) {
        return new DefaultJdbcConnectionMethods(names, jdbcNames);
    }

    @Provides
    public JdbcMethods.JdbcResultSetMethods provideResultSet(final JdbcNames names) {
        return new DefaultJdbcResultSetMethods(names);
    }

    @Provides
    public JdbcMethods.JdbcMetaDataMethods provideMetaData(final JdbcNames names) {
        return new DefaultJdbcMetaDataMethods(names);
    }

    @Provides
    public JdbcMethods.JdbcStatementMethods provideStatement(final JdbcNames names) {
        return new DefaultJdbcStatementMethods(names);
    }

    @Provides
    public JdbcMethods provideJdbcMethods(
            final JdbcMethods.JdbcDataSourceMethods dataSource,
            final JdbcMethods.JdbcConnectionMethods connection,
            final JdbcMethods.JdbcResultSetMethods resultSet,
            final JdbcMethods.JdbcMetaDataMethods metaData,
            final JdbcMethods.JdbcStatementMethods statement) {
        return new DefaultJdbcMethods(dataSource, connection, resultSet, metaData, statement);
    }

    @Provides
    public JdbcNames provideJdbcNames(final RuntimeConfiguration runtimeConfiguration) {
        return new DefaultJdbcNames(runtimeConfiguration.jdbcNames());
    }

    @Provides
    public JdbcBlocks provideJdbcBlocks(
            final RuntimeConfiguration runtimeConfiguration,
            final GenericBlocks blocks,
            final ControlFlows controlFlows,
            final Names names,
            final Variables variables,
            final JdbcNames jdbcNames,
            final JdbcFields jdbcFields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging) {
        return new DefaultJdbcBlocks(
                runtimeConfiguration,
                blocks,
                controlFlows,
                names,
                variables,
                jdbcNames,
                jdbcFields,
                jdbcMethods,
                logging);
    }

}
