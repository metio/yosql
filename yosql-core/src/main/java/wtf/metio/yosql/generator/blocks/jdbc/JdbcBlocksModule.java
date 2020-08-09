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
            final JdbcNamesConfiguration names) {
        return new DefaultJdbcParameters(parameters, names);
    }

    @Provides
    JdbcMethods.JdbcDataSourceMethods provideDataSource(final JdbcNamesConfiguration names) {
        return new DefaultJdbcDataSourceMethods(names);
    }

    @Provides
    JdbcMethods.JdbcConnectionMethods provideConnection(
            final Names names,
            final JdbcNamesConfiguration jdbcNames) {
        return new DefaultJdbcConnectionMethods(names, jdbcNames);
    }

    @Provides
    JdbcMethods.JdbcResultSetMethods provideResultSet(final JdbcNamesConfiguration names) {
        return new DefaultJdbcResultSetMethods(names);
    }

    @Provides
    JdbcMethods.JdbcMetaDataMethods provideMetaData(final JdbcNamesConfiguration names) {
        return new DefaultJdbcMetaDataMethods(names);
    }

    @Provides
    JdbcMethods.JdbcStatementMethods provideStatement(final JdbcNamesConfiguration names) {
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
    JdbcNames provideJdbcNames() {
        return new DefaultJdbcNames();
    }

    @Provides
    JdbcBlocks provideJdbcBlocks(
            final RuntimeConfiguration configuration,
            final GenericBlocks blocks,
            final ControlFlows controlFlows,
            final Names names,
            final Variables variables,
            final JdbcFields jdbcFields,
            final JdbcMethods jdbcMethods,
            @Delegating final LoggingGenerator logging) {
        return new DefaultJdbcBlocks(
                configuration,
                blocks,
                controlFlows,
                names,
                variables,
                jdbcFields,
                jdbcMethods,
                logging);
    }

}
