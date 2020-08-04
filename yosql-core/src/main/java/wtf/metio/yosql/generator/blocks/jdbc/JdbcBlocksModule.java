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
    JdbcTransformer jdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    @Provides
    JdbcFields jdbcFields(final JdbcFieldsConfiguration options) {
        return new DefaultJdbcFields(options);
    }

    @Provides
    JdbcParameters jdbcParameters(final Parameters parameters, final JdbcNamesConfiguration names) {
        return new DefaultJdbcParameters(parameters, names);
    }

    @Provides
    JdbcMethods.JdbcDataSourceMethods dataSource(final JdbcNamesConfiguration names) {
        return new DefaultJdbcDataSourceMethods(names);
    }

    @Provides
    JdbcMethods.JdbcConnectionMethods connection(final Names names, final JdbcNamesConfiguration jdbcNames) {
        return new DefaultJdbcConnectionMethods(names, jdbcNames);
    }

    @Provides
    JdbcMethods.JdbcResultSetMethods resultSet(final JdbcNamesConfiguration names) {
        return new DefaultJdbcResultSetMethods(names);
    }

    @Provides
    JdbcMethods.JdbcMetaDataMethods metaData(final JdbcNamesConfiguration names) {
        return new DefaultJdbcMetaDataMethods(names);
    }

    @Provides
    JdbcMethods.JdbcStatementMethods statement(final JdbcNamesConfiguration names) {
        return new DefaultJdbcStatementMethods(names);
    }

    @Provides
    JdbcMethods jdbcMethods(
            final JdbcMethods.JdbcDataSourceMethods dataSource,
            final JdbcMethods.JdbcConnectionMethods connection,
            final JdbcMethods.JdbcResultSetMethods resultSet,
            final JdbcMethods.JdbcMetaDataMethods metaData,
            final JdbcMethods.JdbcStatementMethods statement) {
        return new DefaultJdbcMethods(dataSource, connection, resultSet, metaData, statement);
    }

    @Provides
    JdbcBlocks jdbcBlocks(
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
