/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql;

import dagger.BindsInstance;
import dagger.Component;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.files.FilesModule;
import wtf.metio.yosql.generator.CodeGeneratorModule;
import wtf.metio.yosql.generator.api.*;
import wtf.metio.yosql.generator.blocks.generic.*;
import wtf.metio.yosql.generator.blocks.jdbc.*;
import wtf.metio.yosql.generator.dao.jdbc.JDBC;
import wtf.metio.yosql.i18n.I18nModule;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.annotations.Generator;
import wtf.metio.yosql.model.annotations.Parser;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.orchestration.OrchestrationModule;

import javax.inject.Singleton;
import java.util.Locale;

/**
 * Configures the entire dependency graph without a {@link RuntimeConfiguration} and {@link Locale} which needs to be
 * provided through the builder method.
 */
@Singleton
@Component(modules = {
        I18nModule.class,
        OrchestrationModule.class,
        FilesModule.class,
        CodeGeneratorModule.class,
        YoSQLModule.class
})
public interface YoSQLComponent {

    /**
     * @return The configured YoSQL instance.
     */
    YoSQL yosql();

    //region visible for testing

    @Delegating
    LoggingGenerator loggingGenerator();

    @Generator
    LocLogger generatorLocLogger();

    @Parser
    LocLogger parserLocLogger();

    AnnotationGenerator annotationGenerator();

    @JDBC
    StandardMethodGenerator jdbcStandardMethodGenerator();

    @JDBC
    BatchMethodGenerator jdbcBatchMethods();

    @JDBC
    Java8StreamMethodGenerator jdbcStreamMethods();

    @JDBC
    RxJavaMethodGenerator jdbcRxjavaMethods();

    Names names();

    Variables variables();

    ControlFlows controlFlows();

    GenericBlocks genericBlocks();

    Parameters parameters();

    Methods methods();

    Classes classes();

    Javadoc javadoc();

    Fields fields();

    @JDBC
    FieldsGenerator jdbcFieldsGenerator();

    @JDBC
    MethodsGenerator jdbcMethodsGenerator();

    JdbcMethods jdbcMethods();

    JdbcNames jdbcNames();

    JdbcFields jdbcFields();

    JdbcParameters jdbcParameters();

    JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods();

    JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods();

    JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods();

    JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods();

    JdbcMethods.JdbcStatementMethods jdbcStatementMethods();

    JdbcBlocks jdbcBlocks();

    JdbcTransformer jdbcTransformer();

    //endregion

    /**
     * Custom builder that allows to inject a user provided {@link RuntimeConfiguration} and {@link Locale}.
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder runtimeConfiguration(RuntimeConfiguration runtimeConfiguration);

        @BindsInstance
        Builder locale(Locale locale);

        YoSQLComponent build();

    }

}
