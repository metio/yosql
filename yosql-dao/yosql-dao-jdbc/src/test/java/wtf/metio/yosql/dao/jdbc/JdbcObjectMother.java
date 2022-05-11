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
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.models.constants.api.PersistenceApis;
import wtf.metio.yosql.models.immutables.ImmutableRuntimeConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;
import wtf.metio.yosql.testing.codegen.Blocks;
import wtf.metio.yosql.testing.configs.*;
import wtf.metio.yosql.testing.logging.LoggingObjectMother;

/**
 * Object mother for JDBC related classes.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">Martin Fowler's article on ObjectMothers</a>
 */
public final class JdbcObjectMother {

    public static JdbcMethods jdbcMethods() {
        final var names = NamesConfigurations.defaults();
        return new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(names),
                new DefaultJdbcConnectionMethods(names),
                new DefaultJdbcDatabaseMetaDataMethods(names),
                new DefaultJdbcResultSetMethods(names),
                new DefaultJdbcResultSetMetaDataMethods(names),
                new DefaultJdbcStatementMethods(names));
    }

    public static FieldsGenerator fieldsGenerator(final JavaConfiguration java) {
        return new JdbcFieldsGenerator(
                ConverterConfigurations.withConverters(),
                NamesConfigurations.defaults(),
                LoggingObjectMother.loggingGenerator(),
                Blocks.javadoc(),
                Blocks.fields(java));
    }

    public static ConstructorGenerator constructorGenerator(final JavaConfiguration java) {
        return new JdbcConstructorGenerator(
                Blocks.genericBlocks(),
                Blocks.methods(java),
                NamesConfigurations.defaults(),
                jdbcParameter(),
                RepositoriesConfigurations.defaults(),
                ConverterConfigurations.withConverters());
    }

    private static DefaultJdbcParameters jdbcParameter() {
        return new DefaultJdbcParameters(Blocks.parameters(), NamesConfigurations.defaults());
    }

    public static JdbcBlocks jdbcBlocks() {
        return jdbcBlocks(JavaConfigurations.defaults());
    }

    public static JdbcBlocks jdbcBlocks(final JavaConfiguration java) {
        return new DefaultJdbcBlocks(
                runtimeConfig(),
                Blocks.genericBlocks(),
                Blocks.controlFlows(java),
                Blocks.variables(java),
                Blocks.fields(java),
                jdbcMethods(),
                LoggingObjectMother.loggingGenerator());
    }

    public static ImmutableRuntimeConfiguration runtimeConfig() {
        return ImmutableRuntimeConfiguration.copyOf(RuntimeConfigurations.defaults())
                .withApi(ApiConfigurations.jul())
                .withFiles(FilesConfigurations.maven())
                .withConverter(ConverterConfigurations.withConverters());
    }

    public static BlockingMethodGenerator blockingMethodGenerator(final JavaConfiguration java) {
        return new JdbcBlockingMethodGenerator(
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer(),
                ConverterConfigurations.withConverters());
    }

    public static BatchMethodGenerator batchMethodGenerator(final JavaConfiguration java) {
        return new JdbcBatchMethodGenerator(
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer());
    }

    public static Java8StreamMethodGenerator java8StreamMethodGenerator(final JavaConfiguration java) {
        return new JdbcJava8StreamMethodGenerator(
                ConverterConfigurations.withConverters(),
                Blocks.controlFlows(java),
                Blocks.methods(java),
                Blocks.parameters(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcTransformer());
    }

    public static RxJavaMethodGenerator rxJavaMethodGenerator(final JavaConfiguration java) {
        return new JdbcRxJavaMethodGenerator(
                ConverterConfigurations.withConverters(),
                Blocks.methods(java),
                Blocks.parameters(java),
                jdbcTransformer(),
                Blocks.controlFlows(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static ReactorMethodGenerator reactorMethodGenerator(final JavaConfiguration java) {
        return new JdbcReactorMethodGenerator(
                ConverterConfigurations.withConverters(),
                Blocks.methods(java),
                Blocks.parameters(java),
                jdbcTransformer(),
                Blocks.controlFlows(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static MutinyMethodGenerator mutinyMethodGenerator(final JavaConfiguration java) {
        return new JdbcMutinyMethodGenerator(
                ConverterConfigurations.withConverters(),
                Blocks.methods(java),
                Blocks.parameters(java),
                jdbcTransformer(),
                Blocks.controlFlows(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java));
    }

    public static JdbcMethodExceptionHandler jdbcTransformer() {
        return new JdbcMethodExceptionHandler();
    }

    public static DelegatingMethodsGenerator delegatingMethodsGenerator(final JavaConfiguration java) {
        return new DelegatingMethodsGenerator(
                constructorGenerator(java),
                blockingMethodGenerator(java),
                batchMethodGenerator(java),
                java8StreamMethodGenerator(java),
                rxJavaMethodGenerator(java),
                reactorMethodGenerator(java),
                mutinyMethodGenerator(java));
    }

    public static GenericRepositoryGenerator genericRepositoryGenerator(final JavaConfiguration java) {
        return new GenericRepositoryGenerator(
                new LocLoggerFactory(new MessageConveyor(SupportedLocales.ENGLISH)).getLocLogger("yosql.test"),
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
