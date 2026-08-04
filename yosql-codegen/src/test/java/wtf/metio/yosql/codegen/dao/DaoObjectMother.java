/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLoggerFactory;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.internals.jdk.SupportedLocales;
import wtf.metio.yosql.codegen.records.JavaSourceParser;
import wtf.metio.yosql.codegen.records.ParameterConversions;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.codegen.records.StatementBinders;
import wtf.metio.yosql.internals.testing.configs.*;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.ImmutableRuntimeConfiguration;
import wtf.metio.yosql.models.immutables.FilesConfiguration;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

/**
 * Object mother for DAO related classes.
 *
 * @see <a href="https://martinfowler.com/bliki/ObjectMother.html">Martin Fowler's article on ObjectMothers</a>
 */
public final class DaoObjectMother {

    public static JdbcMethods jdbcMethods() {
        return new DefaultJdbcMethods(
                new DefaultJdbcDataSourceMethods(),
                new DefaultJdbcConnectionMethods(),
                new DefaultJdbcDatabaseMetaDataMethods(),
                new DefaultJdbcResultSetMethods(),
                new DefaultJdbcResultSetMetaDataMethods(),
                new DefaultJdbcStatementMethods());
    }

    public static FieldsGenerator fieldsGenerator() {
        return fieldsGenerator(RepositoriesConfigurations.defaults());
    }

    public static FieldsGenerator fieldsGenerator(
            final RepositoriesConfiguration repositories) {
        return new DefaultFieldsGenerator(
                ConverterConfigurations.withConverters(),
                repositories,
                LoggingObjectMother.loggingGenerator(),
                BlocksObjectMother.javadoc(),
                BlocksObjectMother.fields());
    }

    public static ConstructorGenerator constructorGenerator() {
        return constructorGenerator(RepositoriesConfigurations.defaults());
    }

    public static ConstructorGenerator constructorGenerator(
            final RepositoriesConfiguration repositories) {
        return new DefaultConstructorGenerator(
                BlocksObjectMother.codeBlocks(),
                BlocksObjectMother.methods(),
                jdbcParameter(),
                repositories,
                ConverterConfigurations.withConverters());
    }

    public static DefaultJdbcParameters jdbcParameter() {
        return new DefaultJdbcParameters(BlocksObjectMother.parameters());
    }

    public static JdbcBlocks jdbcBlocks() {
        return jdbcBlocks(parameterConversions(FilesConfigurations.maven()));
    }

    public static ParameterConversions parameterConversions(final FilesConfiguration files) {
        return new ParameterConversions(
                new RecordScanner(files, new JavaSourceParser()), new StatementBinders());
    }

    public static JdbcBlocks jdbcBlocks(
            final ParameterConversions parameterConversions) {
        return new DefaultJdbcBlocks(
                runtimeConfig(),
                BlocksObjectMother.codeBlocks(),
                BlocksObjectMother.controlFlows(),
                BlocksObjectMother.variables(),
                fieldsGenerator(),
                jdbcMethods(),
                LoggingObjectMother.loggingGenerator(),
                BlocksObjectMother.parameters(),
                BlocksObjectMother.methods(),
                parameterConversions);
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

    public static MethodsGenerator delegatingMethodsGenerator() {
        return delegatingMethodsGenerator(RepositoriesConfigurations.defaults());
    }

    public static MethodsGenerator delegatingMethodsGenerator(
            final RepositoriesConfiguration repositories) {
        return new DefaultMethodsGenerator(
                BlocksObjectMother.javadoc(),
                constructorGenerator(),
                readMethodGenerator(),
                writeMethodGenerator(),
                callMethodGenerator(),
                repositories,
                LoggingObjectMother.logger());
    }

    public static ParameterGenerator parameterGenerator() {
        return new DefaultParameterGenerator(BlocksObjectMother.parameters());
    }

    public static ReturnTypes returnTypes() {
        return new DefaultReturnTypes(ConverterConfigurations.withConverters());
    }

    public static ReadMethodGenerator readMethodGenerator() {
        return new DefaultReadMethodGenerator(
                BlocksObjectMother.controlFlows(),
                BlocksObjectMother.methods(),
                parameterGenerator(),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static WriteMethodGenerator writeMethodGenerator() {
        return new DefaultWriteMethodGenerator(
                BlocksObjectMother.controlFlows(),
                BlocksObjectMother.methods(),
                parameterGenerator(),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static CallMethodGenerator callMethodGenerator() {
        return new DefaultCallMethodGenerator(
                BlocksObjectMother.controlFlows(),
                BlocksObjectMother.methods(),
                parameterGenerator(),
                LoggingObjectMother.loggingGenerator(),
                jdbcBlocks(),
                jdbcMethodExceptionHandler(),
                ConverterConfigurations.withConverters(),
                returnTypes());
    }

    public static RepositoryGenerator defaultRepositoryGenerator() {
        return new DefaultRepositoryGenerator(
                new LocLoggerFactory(new MessageConveyor(SupportedLocales.ENGLISH)).getLocLogger("yosql.test"),
                BlocksObjectMother.annotationGenerator(),
                BlocksObjectMother.classes(),
                BlocksObjectMother.javadoc(),
                DaoObjectMother.fieldsGenerator(),
                DaoObjectMother.delegatingMethodsGenerator());
    }

    /**
     * A whole repository generator wired for one logging API and one set of repository settings.
     *
     * <p>The logging API reaches the generated code from three places at once — the field holding
     * the logger, the guards around each log line, and the JDBC blocks that emit them — so varying
     * it means threading one configuration through all of them rather than swapping a generator.</p>
     */
    public static RepositoryGenerator repositoryGenerator(
            final LoggingConfiguration logging,
            final RepositoriesConfiguration repositories) {
        final var loggingGenerator = LoggingObjectMother.loggingGenerator(logging);
        final var fields = new DefaultFieldsGenerator(
                ConverterConfigurations.withConverters(),
                repositories,
                loggingGenerator,
                BlocksObjectMother.javadoc(),
                BlocksObjectMother.fields());
        final var jdbc = new DefaultJdbcBlocks(
                ImmutableRuntimeConfiguration.copyOf(runtimeConfig()).withLogging(logging),
                BlocksObjectMother.codeBlocks(),
                BlocksObjectMother.controlFlows(),
                BlocksObjectMother.variables(),
                fields,
                jdbcMethods(),
                loggingGenerator,
                BlocksObjectMother.parameters(),
                BlocksObjectMother.methods(),
                parameterConversions(FilesConfigurations.maven()));
        final var methods = new DefaultMethodsGenerator(
                BlocksObjectMother.javadoc(),
                constructorGenerator(repositories),
                new DefaultReadMethodGenerator(BlocksObjectMother.controlFlows(), BlocksObjectMother.methods(),
                        parameterGenerator(), loggingGenerator, jdbc, jdbcMethodExceptionHandler(),
                        ConverterConfigurations.withConverters(), returnTypes()),
                new DefaultWriteMethodGenerator(BlocksObjectMother.controlFlows(), BlocksObjectMother.methods(),
                        parameterGenerator(), loggingGenerator, jdbc, jdbcMethodExceptionHandler(),
                        ConverterConfigurations.withConverters(), returnTypes()),
                new DefaultCallMethodGenerator(BlocksObjectMother.controlFlows(), BlocksObjectMother.methods(),
                        parameterGenerator(), loggingGenerator, jdbc, jdbcMethodExceptionHandler(),
                        ConverterConfigurations.withConverters(), returnTypes()),
                repositories,
                LoggingObjectMother.logger());
        return new DefaultRepositoryGenerator(
                LoggingObjectMother.logger(),
                BlocksObjectMother.annotationGenerator(),
                BlocksObjectMother.classes(),
                BlocksObjectMother.javadoc(),
                fields,
                methods);
    }

    private DaoObjectMother() {
        // factory class
    }

}
