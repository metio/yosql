/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.GenericMethodsGenerator;
import wtf.metio.yosql.models.immutables.ImmutableRuntimeConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.Apis;
import wtf.metio.yosql.testing.configs.Files;
import wtf.metio.yosql.testing.configs.Java;
import wtf.metio.yosql.testing.configs.Jdbc;
import wtf.metio.yosql.testing.logging.Loggers;

public final class JdbcObjectMother {

    public static JdbcMethods jdbcMethods() {
        final var config = jdbcConfig();
        return new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(config),
                new DefaultJdbcConnectionMethods(Blocks.names(), config),
                new DefaultJdbcResultSetMethods(config),
                new DefaultJdbcMetaDataMethods(config),
                new DefaultJdbcStatementMethods(config));
    }

    public static FieldsGenerator fieldsGenerator() {
        return fieldsGenerator(Java.defaults());
    }

    public static FieldsGenerator fieldsGenerator(final JavaConfiguration java) {
        return new JdbcFieldsGenerator(
                jdbcConfig(),
                Loggers.loggingGenerator(),
                Blocks.javadoc(),
                Blocks.fields(java),
                jdbcFields());
    }

    public static DefaultJdbcFields jdbcFields() {
        return new DefaultJdbcFields(jdbcConfig());
    }

    public static ConstructorGenerator constructorGenerator(final JavaConfiguration java) {
        return new JdbcConstructorGenerator(
                Blocks.genericBlocks(),
                Blocks.methods(java),
                jdbcConfig(),
                jdbcParameter());
    }

    private static DefaultJdbcParameters jdbcParameter() {
        return new DefaultJdbcParameters(Blocks.parameters(), jdbcConfig());
    }

    public static JdbcBlocks jdbcBlocks() {
        return jdbcBlocks(Java.defaults());
    }

    public static JdbcBlocks jdbcBlocks(final JavaConfiguration java) {
        return new DefaultJdbcBlocks(
                runtimeConfig(),
                Blocks.genericBlocks(),
                Blocks.controlFlows(java),
                Blocks.names(),
                Blocks.variables(java),
                jdbcConfig(),
                jdbcFields(),
                jdbcMethods(),
                Loggers.loggingGenerator());
    }

    public static ImmutableRuntimeConfiguration runtimeConfig() {
        return RuntimeConfiguration.usingDefaults()
                .setApi(Apis.jul())
                .setFiles(Files.maven())
                .setJdbc(jdbcConfig())
                .build();
    }

    public static StandardMethodGenerator standardMethodGenerator(final JavaConfiguration java) {
        return new JdbcStandardMethodGenerator(
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer(),
                jdbcConfig());
    }

    public static BatchMethodGenerator batchMethodGenerator() {
        return batchMethodGenerator(Java.defaults());
    }

    public static BatchMethodGenerator batchMethodGenerator(final JavaConfiguration java) {
        return new JdbcBatchMethodGenerator(
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer());
    }

    public static Java8StreamMethodGenerator java8StreamMethodGenerator(final JavaConfiguration java) {
        return new JdbcJava8StreamMethodGenerator(
                jdbcConfig(),
                Blocks.genericBlocks(),
                Blocks.controlFlows(java),
                Blocks.names(),
                Blocks.methods(java),
                Blocks.parameters(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer());
    }

    public static RxJavaMethodGenerator rxJavaMethodGenerator(final JavaConfiguration java) {
        return new JdbcRxJavaMethodGenerator(
                jdbcConfig(),
                Blocks.controlFlows(java),
                Blocks.names(),
                Blocks.methods(java),
                Blocks.parameters(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static JdbcConfiguration jdbcConfig() {
        return Jdbc.withResultRowConverter();
    }

    public static DefaultJdbcTransformer jdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    public static GenericMethodsGenerator genericMethodsGenerator() {
        return genericMethodsGenerator(Java.defaults());
    }

    public static GenericMethodsGenerator genericMethodsGenerator(final JavaConfiguration java) {
        return new GenericMethodsGenerator(
                constructorGenerator(java),
                standardMethodGenerator(java),
                batchMethodGenerator(java),
                java8StreamMethodGenerator(java),
                rxJavaMethodGenerator(java));
    }

    private JdbcObjectMother() {
        // factory class
    }

}
