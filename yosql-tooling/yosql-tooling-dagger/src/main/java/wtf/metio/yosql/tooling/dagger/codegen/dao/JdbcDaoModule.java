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
import wtf.metio.yosql.codegen.blocks.GenericMethodsGenerator;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.codegen.logging.Generator;
import wtf.metio.yosql.dao.jdbc.*;
import wtf.metio.yosql.dao.jdbc.utilities.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.tooling.dagger.codegen.blocks.DefaultJdbcBlocksModule;

/**
 * Dagger module for the JDBC based DAO implementation.
 */
@Module(includes = DefaultJdbcBlocksModule.class)
public class JdbcDaoModule {

    @IntoSet
    @Provides
    public RepositoryGenerator provideRepositoryGenerator(
            final @Generator LocLogger logger,
            final AnnotationGenerator annotations,
            final Classes classes,
            final Javadoc javadoc,
            final @JDBC FieldsGenerator fields,
            final @JDBC MethodsGenerator methods) {
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
    public MethodsGenerator provideMethodsGenerator(
            final @JDBC BatchMethodGenerator batchMethods,
            final @JDBC Java8StreamMethodGenerator streamMethods,
            final @JDBC RxJavaMethodGenerator rxjavaMethods,
            final @JDBC StandardMethodGenerator standardMethods,
            final @JDBC ConstructorGenerator constructor) {
        return new GenericMethodsGenerator(
                constructor,
                standardMethods,
                batchMethods,
                streamMethods,
                rxjavaMethods
        );
    }

    @JDBC
    @Provides
    public ConstructorGenerator provideConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final RuntimeConfiguration runtimeConfiguration,
            final JdbcParameters jdbcParameters) {
        return new JdbcConstructorGenerator(
                blocks,
                methods,
                runtimeConfiguration.names(),
                jdbcParameters,
                runtimeConfiguration.repositories());
    }

    @JDBC
    @Provides
    public FieldsGenerator provideFieldsGenerator(
            final Fields fields,
            @Delegating final LoggingGenerator logging,
            final JdbcFields jdbcFields,
            final Javadoc javadoc,
            final RuntimeConfiguration runtimeConfiguration,
            final NamesConfiguration names) {
        return new JdbcFieldsGenerator(
                runtimeConfiguration.jdbc(),
                names,
                logging,
                javadoc,
                fields,
                jdbcFields);
    }

    @JDBC
    @Provides
    public BatchMethodGenerator provideBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer transformer) {
        return new JdbcBatchMethodGenerator(
                controlFlow,
                methods,
                parameters,
                logging,
                jdbc,
                transformer);
    }

    @JDBC
    @Provides
    public Java8StreamMethodGenerator provideJava8StreamMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer) {
        return new JdbcJava8StreamMethodGenerator(
                runtimeConfiguration.jdbc(),
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

    @JDBC
    @Provides
    public RxJavaMethodGenerator provideRxJavaMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        return new JdbcRxJavaMethodGenerator(
                runtimeConfiguration.jdbc(),
                controlFlows,
                names,
                methods,
                parameters,
                logging,
                jdbcBlocks);
    }

    @JDBC
    @Provides
    public StandardMethodGenerator provideStandardMethodGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            @Delegating final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        return new JdbcStandardMethodGenerator(
                controlFlows,
                methods,
                parameters,
                logging,
                jdbc,
                jdbcTransformer,
                runtimeConfiguration.jdbc());
    }

    @Provides
    UtilitiesGenerator provideUtilitiesGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final FlowStateGenerator flowStateGenerator,
            final ResultStateGenerator resultStateGenerator,
            final ToResultRowConverterGenerator toResultRowConverterGenerator,
            final ResultRowGenerator resultRowGenerator) {
        return new JdbcUtilitiesGenerator(
                flowStateGenerator,
                resultStateGenerator,
                toResultRowConverterGenerator,
                resultRowGenerator,
                runtimeConfiguration.jdbc());
    }

}
