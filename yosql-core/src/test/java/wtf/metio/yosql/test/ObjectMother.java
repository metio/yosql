/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.test;

import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.DaggerYoSQLComponent;
import wtf.metio.yosql.YoSQLComponent;
import wtf.metio.yosql.generator.api.*;
import wtf.metio.yosql.generator.blocks.generic.*;
import wtf.metio.yosql.generator.blocks.jdbc.*;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.sql.ResultRowConverter;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlParameter;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public final class ObjectMother {

    public static SqlConfiguration sqlConfiguration() {
        final var config = new SqlConfiguration();
        config.setName("queryTest");
        config.setParameters(List.of(SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(0)
                .setConverter("defaultRowConverter")
                .build()));
        config.setResultRowConverter(ResultRowConverter.builder()
                .setAlias("defaultRowConverter")
                .setConverterType("com.example.DefaultConverter")
                .setResultType(Object.class.getName())
                .setMethodName("apply")
                .build());
        config.setRepository("Test");
        return config;
    }

    public static List<SqlStatement> sqlStatements() {
        return List.of(SqlStatement.builder()
                .setSourcePath(Paths.get("/some/path/query.sql"))
                .setRawStatement("SELECT raw FROM table;")
                .setConfiguration(sqlConfiguration())
                .build());
    }

    public static LoggingGenerator loggingGenerator() {
        return loggingGenerator(RuntimeConfiguration.usingDefaults());
    }

    public static LoggingGenerator loggingGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).loggingGenerator();
    }

    public static Names names() {
        return names(RuntimeConfiguration.usingDefaults());
    }

    public static Names names(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).names();
    }

    public static Variables variables() {
        return variables(RuntimeConfiguration.usingDefaults());
    }

    public static Variables variables(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).variables();
    }

    public static ControlFlows controlFlows() {
        return controlFlows(RuntimeConfiguration.usingDefaults());
    }

    public static ControlFlows controlFlows(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).controlFlows();
    }

    public static GenericBlocks genericBlocks() {
        return genericBlocks(RuntimeConfiguration.usingDefaults());
    }

    public static GenericBlocks genericBlocks(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).genericBlocks();
    }

    public static Parameters parameters() {
        return parameters(RuntimeConfiguration.usingDefaults());
    }

    public static Parameters parameters(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).parameters();
    }

    public static AnnotationGenerator annotationGenerator() {
        return annotationGenerator(RuntimeConfiguration.usingDefaults());
    }

    public static AnnotationGenerator annotationGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).annotationGenerator();
    }

    public static Methods methods() {
        return methods(RuntimeConfiguration.usingDefaults());
    }

    public static Methods methods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).methods();
    }

    public static JdbcMethods jdbcMethods() {
        return jdbcMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods jdbcMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcMethods();
    }

    public static JdbcFields jdbcFields() {
        return jdbcFields(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcFields jdbcFields(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcFields();
    }

    public static JdbcNames jdbcNames() {
        return jdbcNames(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcNames jdbcNames(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcNames();
    }

    public static JdbcParameters jdbcParameters() {
        return jdbcParameters(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcParameters jdbcParameters(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcParameters();
    }

    public static JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods() {
        return jdbcDataSourceMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods.JdbcDataSourceMethods jdbcDataSourceMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcDataSourceMethods();
    }

    public static JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods() {
        return jdbcConnectionMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods.JdbcConnectionMethods jdbcConnectionMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcConnectionMethods();
    }

    public static JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods() {
        return jdbcResultSetMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods.JdbcResultSetMethods jdbcResultSetMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcResultSetMethods();
    }

    public static JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods() {
        return jdbcMetaDataMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods.JdbcMetaDataMethods jdbcMetaDataMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcMetaDataMethods();
    }

    public static JdbcMethods.JdbcStatementMethods jdbcStatementMethods() {
        return jdbcStatementMethods(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcMethods.JdbcStatementMethods jdbcStatementMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcStatementMethods();
    }

    public static StandardMethodGenerator jdbcStandardMethodGenerator() {
        return jdbcStandardMethodGenerator(RuntimeConfiguration.usingDefaults());
    }

    public static StandardMethodGenerator jdbcStandardMethodGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcStandardMethodGenerator();
    }

    public static BatchMethodGenerator jdbcBatchMethods() {
        return jdbcBatchMethods(RuntimeConfiguration.usingDefaults());
    }

    public static BatchMethodGenerator jdbcBatchMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcBatchMethods();
    }

    public static Java8StreamMethodGenerator jdbcStreamMethods() {
        return jdbcStreamMethods(RuntimeConfiguration.usingDefaults());
    }

    public static Java8StreamMethodGenerator jdbcStreamMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcStreamMethods();
    }

    public static RxJavaMethodGenerator jdbcRxjavaMethods() {
        return jdbcRxjavaMethods(RuntimeConfiguration.usingDefaults());
    }

    public static RxJavaMethodGenerator jdbcRxjavaMethods(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcRxjavaMethods();
    }

    public static ConstructorGenerator jdbcConstructor() {
        return jdbcConstructor(RuntimeConfiguration.usingDefaults());
    }

    public static ConstructorGenerator jdbcConstructor(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcConstructor();
    }

    public static LocLogger generatorLocLogger() {
        return generatorLocLogger(RuntimeConfiguration.usingDefaults());
    }

    public static LocLogger generatorLocLogger(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).generatorLocLogger();
    }

    public static LocLogger parserLocLogger() {
        return parserLocLogger(RuntimeConfiguration.usingDefaults());
    }

    public static LocLogger parserLocLogger(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).parserLocLogger();
    }

    public static Classes classes() {
        return classes(RuntimeConfiguration.usingDefaults());
    }

    public static Classes classes(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).classes();
    }

    public static Javadoc javadoc() {
        return javadoc(RuntimeConfiguration.usingDefaults());
    }

    public static Javadoc javadoc(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).javadoc();
    }

    public static FieldsGenerator jdbcFieldsGenerator() {
        return jdbcFieldsGenerator(RuntimeConfiguration.usingDefaults());
    }

    public static FieldsGenerator jdbcFieldsGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcFieldsGenerator();
    }

    public static MethodsGenerator jdbcMethodsGenerator() {
        return jdbcMethodsGenerator(RuntimeConfiguration.usingDefaults());
    }

    public static MethodsGenerator jdbcMethodsGenerator(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcMethodsGenerator();
    }

    public static Fields fields() {
        return fields(RuntimeConfiguration.usingDefaults());
    }

    public static Fields fields(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).fields();
    }

    public static JdbcBlocks jdbcBlocks() {
        return jdbcBlocks(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcBlocks jdbcBlocks(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcBlocks();
    }

    public static JdbcTransformer jdbcTransformer() {
        return jdbcTransformer(RuntimeConfiguration.usingDefaults());
    }

    public static JdbcTransformer jdbcTransformer(final RuntimeConfiguration runtimeConfiguration) {
        return yoSQLComponent(runtimeConfiguration).jdbcTransformer();
    }

    private static YoSQLComponent yoSQLComponent(final RuntimeConfiguration runtimeConfiguration) {
        return DaggerYoSQLComponent.builder()
                .locale(Locale.ENGLISH)
                .runtimeConfiguration(runtimeConfiguration)
                .build();
    }

    private ObjectMother() {
        // factory class
    }


}
