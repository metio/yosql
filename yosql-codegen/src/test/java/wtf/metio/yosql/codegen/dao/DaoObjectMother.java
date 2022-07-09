/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.internals.testing.configs.*;
import wtf.metio.yosql.models.immutables.ImmutableRuntimeConfiguration;
import wtf.metio.yosql.models.immutables.JavaConfiguration;

/**
 * Object mother for DAO related classes.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">Martin Fowler's article on ObjectMothers</a>
 */
public final class DaoObjectMother {

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
        return new DefaultFieldsGenerator(
                ConverterConfigurations.withConverters(),
                NamesConfigurations.defaults(),
                LoggingObjectMother.loggingGenerator(),
                BlocksObjectMother.javadoc(),
                BlocksObjectMother.fields(java));
    }

    public static ConstructorGenerator constructorGenerator(final JavaConfiguration java) {
        return new DefaultConstructorGenerator(
                BlocksObjectMother.codeBlocks(),
                BlocksObjectMother.methods(java),
                NamesConfigurations.defaults(),
                jdbcParameter(java),
                RepositoriesConfigurations.defaults(),
                ConverterConfigurations.withConverters());
    }

    public static DefaultJdbcParameters jdbcParameter(final JavaConfiguration java) {
        return new DefaultJdbcParameters(BlocksObjectMother.parameters(java), NamesConfigurations.defaults());
    }

    public static JdbcBlocks jdbcBlocks(final JavaConfiguration java) {
        return new DefaultJdbcBlocks(
                runtimeConfig(),
                BlocksObjectMother.codeBlocks(),
                BlocksObjectMother.controlFlows(java),
                BlocksObjectMother.variables(java),
                fieldsGenerator(java),
                jdbcMethods(),
                LoggingObjectMother.loggingGenerator(),
                BlocksObjectMother.parameters(java),
                BlocksObjectMother.methods(java));
    }

    public static ImmutableRuntimeConfiguration runtimeConfig() {
        return ImmutableRuntimeConfiguration.copyOf(RuntimeConfigurations.defaults())
                .withLogging(LoggingConfigurations.jul())
                .withFiles(FilesConfigurations.maven())
                .withConverter(ConverterConfigurations.withConverters());
    }

    public static DefaultMethodExceptionHandler jdbcMethodExceptionHandler() {
        return new DefaultMethodExceptionHandler();
    }

    public static MethodsGenerator delegatingMethodsGenerator(final JavaConfiguration java) {
        return new DefaultMethodsGenerator(
                constructorGenerator(java),
                readMethodGenerator(java),
                writeMethodGenerator(java),
                callMethodGenerator(java),
                LoggingObjectMother.logger());
    }

    public static ParameterGenerator parameterGenerator(final JavaConfiguration java) {
        return new DefaultParameterGenerator(BlocksObjectMother.parameters(java), NamesConfigurations.defaults());
    }

    public static ReturnTypes returnTypes() {
        return new DefaultReturnTypes(ConverterConfigurations.withConverters());
    }

    public static ReadMethodGenerator readMethodGenerator(final JavaConfiguration java) {
        return new DefaultReadMethodGenerator(
                BlocksObjectMother.controlFlows(java),
                BlocksObjectMother.methods(java),
                parameterGenerator(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static WriteMethodGenerator writeMethodGenerator(final JavaConfiguration java) {
        return new DefaultWriteMethodGenerator(
                BlocksObjectMother.controlFlows(java),
                BlocksObjectMother.methods(java),
                parameterGenerator(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static CallMethodGenerator callMethodGenerator(final JavaConfiguration java) {
        return new DefaultCallMethodGenerator(
                BlocksObjectMother.controlFlows(java),
                BlocksObjectMother.methods(java),
                parameterGenerator(java),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(java),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static RepositoryGenerator defaultRepositoryGenerator(final JavaConfiguration java) {
        return new DefaultRepositoryGenerator(
                new LocLoggerFactory(new MessageConveyor(SupportedLocales.ENGLISH)).getLocLogger("yosql.test"),
                BlocksObjectMother.annotationGenerator(),
                BlocksObjectMother.classes(java),
                BlocksObjectMother.javadoc(),
                DaoObjectMother.fieldsGenerator(java),
                DaoObjectMother.delegatingMethodsGenerator(java));
    }

    private DaoObjectMother() {
        // factory class
    }

}
