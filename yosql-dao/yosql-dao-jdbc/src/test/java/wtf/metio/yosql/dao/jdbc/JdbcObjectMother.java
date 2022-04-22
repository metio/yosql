/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.codegen.blocks.GenericRepositoryGenerator;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ImmutableRuntimeConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.models.immutables.JdbcConfiguration;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.*;
import wtf.metio.yosql.testing.logging.Loggers;

import java.util.Locale;

/**
 * Object mother for JDBC related classes.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">Martin Fowler's article on ObjectMothers</a>
 */
public final class JdbcObjectMother {

    public static JdbcMethods jdbcMethods() {
        final var names = Names.defaults();
        return new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(names),
                new DefaultJdbcConnectionMethods(names),
                new DefaultJdbcDatabaseMetaDataMethods(names),
                new DefaultJdbcResultSetMethods(names),
                new DefaultJdbcResultSetMetaDataMethods(names),
                new DefaultJdbcStatementMethods(names));
    }

    public static FieldsGenerator fieldsGenerator() {
        return fieldsGenerator(Java.defaults());
    }

    public static FieldsGenerator fieldsGenerator(final JavaConfiguration java) {
        return new JdbcFieldsGenerator(
                jdbcConfig(),
                Names.defaults(),
                Loggers.loggingGenerator(),
                Blocks.javadoc(),
                Blocks.fields(java),
                jdbcFields());
    }

    public static DefaultJdbcFields jdbcFields() {
        return new DefaultJdbcFields(Names.defaults());
    }

    public static ConstructorGenerator constructorGenerator(final JavaConfiguration java) {
        return new JdbcConstructorGenerator(
                Blocks.genericBlocks(),
                Blocks.methods(java),
                Names.defaults(),
                jdbcParameter(),
                Repositories.defaults());
    }

    private static DefaultJdbcParameters jdbcParameter() {
        return new DefaultJdbcParameters(Blocks.parameters(), Names.defaults());
    }

    public static JdbcBlocks jdbcBlocks() {
        return jdbcBlocks(Java.defaults());
    }

    public static JdbcBlocks jdbcBlocks(final JavaConfiguration java) {
        return new DefaultJdbcBlocks(
                runtimeConfig(),
                Blocks.genericBlocks(),
                Blocks.controlFlows(java),
                Names.defaults(),
                Blocks.variables(java),
                jdbcConfig(),
                jdbcFields(),
                jdbcMethods(),
                Loggers.loggingGenerator());
    }

    public static ImmutableRuntimeConfiguration runtimeConfig() {
        return ImmutableRuntimeConfiguration.copyOf(Configs.runtime())
                .withApi(Apis.jul())
                .withFiles(Files.maven())
                .withJdbc(jdbcConfig());
    }

    public static BlockingMethodGenerator blockingMethodGenerator(final JavaConfiguration java) {
        return new JdbcBlockingMethodGenerator(
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
                Names.defaults(),
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
                Names.defaults(),
                Blocks.methods(java),
                Blocks.parameters(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static ReactorMethodGenerator reactorMethodGenerator() {
        return new JdbcReactorMethodGenerator();
    }

    public static MutinyMethodGenerator mutinyMethodGenerator(final JavaConfiguration java) {
        return new JdbcMutinyMethodGenerator(
                jdbcConfig(),
                Blocks.methods(java),
                Blocks.parameters(java),
                jdbcTransformer(),
                Blocks.controlFlows(java),
                Loggers.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static JdbcConfiguration jdbcConfig() {
        return Jdbc.withResultRowConverter();
    }

    public static DefaultJdbcTransformer jdbcTransformer() {
        return new DefaultJdbcTransformer();
    }

    public static DelegatingMethodsGenerator delegatingMethodsGenerator() {
        return delegatingMethodsGenerator(Java.defaults());
    }

    public static DelegatingMethodsGenerator delegatingMethodsGenerator(final JavaConfiguration java) {
        return new DelegatingMethodsGenerator(
                constructorGenerator(java),
                blockingMethodGenerator(java),
                batchMethodGenerator(java),
                java8StreamMethodGenerator(java),
                rxJavaMethodGenerator(java),
                reactorMethodGenerator(),
                mutinyMethodGenerator(java));
    }

    public static GenericRepositoryGenerator genericRepositoryGenerator() {
        return genericRepositoryGenerator(Java.defaults());
    }

    public static GenericRepositoryGenerator genericRepositoryGenerator(final JavaConfiguration java) {
        return new GenericRepositoryGenerator(
                new LocLoggerFactory(new MessageConveyor(Locale.ENGLISH)).getLocLogger("yosql.test"),
                Blocks.annotationGenerator(),
                Blocks.classes(java),
                Blocks.javadoc(),
                JdbcObjectMother.fieldsGenerator(java),
                JdbcObjectMother.delegatingMethodsGenerator(java),
                PersistenceApis.JDBC);
    }

    private JdbcObjectMother() {
        // factory class
    }

}
