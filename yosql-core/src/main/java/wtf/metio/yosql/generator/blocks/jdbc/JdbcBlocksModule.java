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
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.configuration.JdbcFieldsConfiguration;
import wtf.metio.yosql.model.configuration.JdbcNamesConfiguration;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

@Module
public class JdbcBlocksModule {

    @Provides
    JdbcTransformer provideJdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    @Provides
    JdbcFields provideJdbcFields(final JdbcFieldsConfiguration options) {
        return new DefaultJdbcFields(options);
    }

    @Provides
    JdbcParameters provideJdbcParameters(
            final Parameters parameters,
            final JdbcNames names) {
        return new DefaultJdbcParameters(parameters, names);
    }

    @Provides
    JdbcMethods.JdbcDataSourceMethods provideDataSource(final JdbcNames names) {
        return new DefaultJdbcDataSourceMethods(names);
    }

    @Provides
    JdbcMethods.JdbcConnectionMethods provideConnection(
            final Names names,
            final JdbcNames jdbcNames) {
        return new DefaultJdbcConnectionMethods(names, jdbcNames);
    }

    @Provides
    JdbcMethods.JdbcResultSetMethods provideResultSet(final JdbcNames names) {
        return new DefaultJdbcResultSetMethods(names);
    }

    @Provides
    JdbcMethods.JdbcMetaDataMethods provideMetaData(final JdbcNames names) {
        return new DefaultJdbcMetaDataMethods(names);
    }

    @Provides
    JdbcMethods.JdbcStatementMethods provideStatement(final JdbcNames names) {
        return new DefaultJdbcStatementMethods(names);
    }

    @Provides
    JdbcMethods provideJdbcMethods(
            final JdbcMethods.JdbcDataSourceMethods dataSource,
            final JdbcMethods.JdbcConnectionMethods connection,
            final JdbcMethods.JdbcResultSetMethods resultSet,
            final JdbcMethods.JdbcMetaDataMethods metaData,
            final JdbcMethods.JdbcStatementMethods statement) {
        return new DefaultJdbcMethods(dataSource, connection, resultSet, metaData, statement);
    }

    @Provides
    JdbcNames provideJdbcNames(final JdbcNamesConfiguration configuration) {
        return new DefaultJdbcNames(configuration);
    }

    @Provides
    JdbcBlocks provideJdbcBlocks(
            final RuntimeConfiguration configuration,
            final GenericBlocks blocks,
            final ControlFlows controlFlows,
            final Names names,
            final Variables variables,
            final JdbcNames jdbcNames,
            final JdbcFields jdbcFields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging) {
        return new DefaultJdbcBlocks(
                configuration,
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
