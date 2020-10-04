/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql;

import dagger.Component;
import wtf.metio.yosql.files.FilesModule;
import wtf.metio.yosql.generator.CodeGeneratorModule;
import wtf.metio.yosql.generator.api.AnnotationGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.api.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcFields;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcMethods;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcNames;
import wtf.metio.yosql.i18n.I18nModule;
import wtf.metio.yosql.i18n.Translator;
import wtf.metio.yosql.model.annotations.Delegating;
import wtf.metio.yosql.model.configuration.*;
import wtf.metio.yosql.orchestration.OrchestrationModule;

import javax.inject.Singleton;

/**
 * Dagger interface to get a new YoSQL instance based on the default configuration and
 * built-in default implementations of the various YoSQL interfaces.
 */
@Singleton
@Component(modules = {
        I18nModule.class,
        ModelConfigurationModule.class,
        OrchestrationModule.class,
        FilesModule.class,
        CodeGeneratorModule.class,
        YoSqlModule.class,
})
public interface YoSqlComponent {

    /**
     * @return The YoSQL instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    YoSql yosql();

    //region used by tests

    /**
     * @return The Translator instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    Translator translator();

    /**
     * @return The RuntimeConfiguration instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    RuntimeConfiguration runtimeConfiguration();

    /**
     * @return The JdbcNamesConfiguration instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcNamesConfiguration jdbcNamesConfiguration();

    /**
     * @return The JdbcFieldsConfiguration instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcFieldsConfiguration jdbcFieldsConfiguration();

    /**
     * @return The VariableConfiguration instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    VariableConfiguration variableConfiguration();

    /**
     * @return The AnnotationConfiguration instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    AnnotationConfiguration annotationConfiguration();

    /**
     * @return The GenericBlocks instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    GenericBlocks genericBlocks();

    /**
     * @return The ControlFlows instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    ControlFlows controlFlows();

    /**
     * @return The Parameters instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    Parameters parameters();

    /**
     * @return The Fields instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    Fields fields();

    /**
     * @return The Variables instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    Variables variables();

    /**
     * @return The Names instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    Names names();

    /**
     * @return The AnnotationGenerator instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    AnnotationGenerator annotationGenerator();

    /**
     * @return The LoggingGenerator instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    @Delegating
    LoggingGenerator loggingGenerator();

    /**
     * @return The JdbcFields instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcFields jdbcFields();

    /**
     * @return The JdbcMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods jdbcMethods();

    /**
     * @return The JdbcDataSourceMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods();

    /**
     * @return The JdbcConnectionMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods();

    /**
     * @return The JdbcResultSetMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods();

    /**
     * @return The JdbcMetaDataMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods();

    /**
     * @return The JdbcStatementMethods instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcMethods.JdbcStatementMethods jdbcStatementMethods();

    /**
     * @return The JdbcNames instance configured by the list of modules specified in the
     * <code>@Component</code> class annotation.
     */
    JdbcNames jdbcNames();

    //endregion

}
