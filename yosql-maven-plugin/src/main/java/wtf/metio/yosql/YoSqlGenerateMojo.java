/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import javax.annotation.processing.Generated;

import com.squareup.javapoet.ClassName;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import ch.qos.cal10n.IMessageConveyor;
import ch.qos.cal10n.MessageConveyor;
import wtf.metio.yosql.generator.utilities.ToResultRowConverterGenerator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.model.errors.ExecutionErrors;
import wtf.metio.yosql.model.sql.ParameterConverter;
import wtf.metio.yosql.model.sql.ResultRowConverter;

/**
 * The *generate* goal generates Java code based on SQL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YoSqlGenerateMojo extends AbstractMojo {

    /**
     * The SQL files to load (.sql)
     */
    @Parameter
    private FileSet                        sqlFiles;

    /**
     * The output directory for the generated classes
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/yosql")
    private File                           outputBaseDirectory;

    /**
     * The package name for utility classes (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String                         utilityPackageName;

    /**
     * The package name for utility classes (default: <strong>converter</strong>).
     */
    @Parameter(required = true, defaultValue = "converter")
    private String                         converterPackageName;

    /**
     * The base package name for all generated classes (default: <strong>com.example.persistence</strong>).
     */
    @Parameter(required = true, defaultValue = "com.example.persistence")
    private String                         basePackageName;

    /**
     * Controls whether the SQL statements should be inlined in the generated repositories or loaded at options
     * (default: <strong>inline</strong>). Other possible value is <strong>load</strong>.
     */
    @Parameter(required = true, defaultValue = "inline")
    private String                         repositorySqlStatements;

    /**
     * Controls whether the generated repositories should contain <em>generic</em> methods that. Standard methods
     * execute depending on the type of the query and could either be a single 'executeQuery' on a PreparedStatement in
     * case of SQL SELECT statements or a single call to 'executeUpdate' for SQL UPDATE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodStandardApi;

    /**
     * Controls whether the generated repositories should contain batch methods for SQL INSERT/UPDATE/DELETE statements.
     * (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodBatchApi;

    /**
     * The method name prefix to apply to all batch methods (default: <strong>""</strong>).
     */
    @Parameter
    private String                         methodBatchPrefix;

    /**
     * The method name suffix to apply to all batch methods (default: <strong>"Batch"</strong>).
     */
    @Parameter(required = true, defaultValue = "Batch")
    private String                         methodBatchSuffix;

    /**
     * Controls whether an eager {@link Stream} based method should be generated (default: <strong>true</strong>). If
     * the target Java version is set to anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodStreamEagerApi;

    /**
     * Controls whether a lazy {@link Stream} based method should be generated (default: <strong>true</strong>). If the
     * target Java version is set to anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodStreamLazyApi;

    /**
     * Controls whether a RxJava {@link io.reactivex.Flowable} based method should be generated (default:
     * <strong>false</strong>). In case <strong>io.reactivex.rxjava2:rxjava</strong> is a declared dependency, defaults
     * to <strong>true</strong>.
     */
    @Parameter
    private Boolean                        methodRxJavaApi;

    /**
     * The method name prefix to apply to all stream methods (default: <strong>""</strong>).
     */
    @Parameter
    private String                         methodStreamPrefix;

    /**
     * The method name suffix to apply to all stream methods (default: <strong>Stream</strong>).
     */
    @Parameter(required = true, defaultValue = "Stream")
    private String                         methodStreamSuffix;

    /**
     * The method name prefix to apply to all RxJava methods (default: <strong>""</strong>).
     */
    @Parameter
    private String                         methodRxJavaPrefix;

    /**
     * The method name suffix to apply to all RxJava methods (default: <strong>Flow</strong>).
     */
    @Parameter(required = true, defaultValue = "Flow")
    private String                         methodRxJavaSuffix;

    /**
     * The method name extra to apply to all lazy stream methods (default: <strong>Lazy</strong>).
     */
    @Parameter(required = true, defaultValue = "Lazy")
    private String                         methodLazyName;

    /**
     * The method name extra to apply to all eager stream methods (default: <strong>Eager</strong>).
     */
    @Parameter(required = true, defaultValue = "Eager")
    private String                         methodEagerName;

    /**
     * The repository name suffix to use for all generated repositories (default: <strong>Repository</strong>).
     */
    @Parameter(required = true, defaultValue = "Repository")
    private String                         repositoryNameSuffix;

    /**
     * Controls whether an interface should be generated for each generated repository. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        repositoryGenerateInterface;

    /**
     * The allow method name prefixes for writing methods (default: <strong>"update, insert, delete, create, write, add,
     * remove, merge,drop"</strong>).
     */
    @Parameter(required = true, defaultValue = "update,insert,delete,create,write,add,remove,merge,drop")
    private String                         methodAllowedWritePrefixes;

    /**
     * The allow method name prefixes for writing methods (default: <strong>"select,read,query,find"</strong>).
     */
    @Parameter(required = true, defaultValue = "select,read,query,find")
    private String                         methodAllowedReadPrefixes;

    /**
     * The allow method name prefixes for writing methods (default: <strong>"select,read,query,find"</strong>).
     */
    @Parameter(required = true, defaultValue = "call,execute")
    private String                         methodAllowedCallPrefixes;

    /**
     * Controls whether method names are validated according to <strong>methodAllowedReadPrefixes</strong> and
     * <strong>methodAllowedWritePrefixes</strong> (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodValidateNamePrefixes;

    /**
     * The groupId to match for automatic RxJava detection (default: <strong>"io.reactivex.rxjava2"</strong>).
     */
    @Parameter(required = true, defaultValue = "io.reactivex.rxjava2")
    private String                         rxJavaGroupId;

    /**
     * The artifactId to match for automatic RxJava detection (default: <strong>"rxjava"</strong>).
     */
    @Parameter(required = true, defaultValue = "rxjava")
    private String                         rxJavaArtifactId;

    /**
     * The groupId to match for automatic Log4j detection (default: <strong>"org.apache.logging.log4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.apache.logging.log4j")
    private String                         log4jGroupId;

    /**
     * The artifactId to match for automatic Log4j detection (default: <strong>"log4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "log4j-api")
    private String                         log4jArtifactId;

    /**
     * The groupId to match for automatic SLF4j detection (default: <strong>"org.slf4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.slf4j")
    private String                         slf4jGroupId;

    /**
     * The artifactId to match for automatic SLF4j detection (default: <strong>"slf4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "slf4j-api")
    private String                         slf4jArtifactId;

    /**
     * The separator to split SQL statements inside a single .sql file (default: <strong>";"</strong>).
     */
    @Parameter(required = true, defaultValue = ";")
    private String                         sqlStatementSeparator;

    /**
     * The charset to use while reading .sql files (default: <strong>UTF-8</strong>).
     */
    @Parameter(required = true, defaultValue = "UTF-8")
    private String                         sqlFilesCharset;

    /**
     * The target Java source version (default: <strong>1.8</strong>). A value lower than 1.8 disables the generation of
     * the 'java.util.Stream' based API.
     */
    @Parameter(required = true, defaultValue = "1.8")
    private String                         java;

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the logging API based on the projects
     * dependencies). Possible other values are "jdk", "log4j", "slf4j" and "none".
     */
    @Parameter(required = true, defaultValue = "auto")
    private String                         loggingApi;

    /**
     * Whether generated methods should catch SqlExceptions and rethrow them as RuntimeExceptions (default:
     * <strong>true</strong>). If set to <strong>false</strong>, this will cause methods to declare that they throw a
     * checked exception which in turn will force all its users to handle the exception.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        methodCatchAndRethrow;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter
    private final List<ParameterConverter> parameterConverters = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter
    private final List<ResultRowConverter> resultRowConverters = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is specified for a statement. Can be either
     * the alias or fully-qualified name of a converter. Default 'resultRow'.
     */
    @Parameter(required = true, defaultValue = ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME)
    private String                         defaultRowConverter;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated repositories.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                        classGeneratedAnnotation;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated fields.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean                        fieldGeneratedAnnotation;

    /**
     * Controls whether {@link Generated} annotations should be added to the generated methods.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean                        methodGeneratedAnnotation;

    /**
     * Sets the comment used for {@link Generated} annotations.
     */
    @Parameter(required = true, defaultValue = "DO NOT EDIT")
    private String                         generatedAnnotationComment;

    @Override
    public void execute() {
        final IMessageConveyor messages = new MessageConveyor(Locale.ENGLISH);
        final ExecutionErrors errors = new ExecutionErrors();
        final RuntimeConfiguration configuration = createConfiguration(messages, errors);
        final YoSql yoSql = createYoSql(configuration, errors);

        yoSql.generateCode();
    }

    private YoSql createYoSql(final RuntimeConfiguration configuration, final ExecutionErrors errors) {
        // TODO Auto-generated method stub
        return null;
    }

    private RuntimeConfiguration createConfiguration(final IMessageConveyor messages, final ExecutionErrors errors) {
        final int parsedJavaVersion = Integer.parseInt(java.substring(java.length() - 1, java.length()));

        final RuntimeConfiguration.Builder builder = RuntimeConfiguration.builder()
                .setOutputBaseDirectory(outputBaseDirectory.toPath())
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                .setSqlStatementSeparator(sqlStatementSeparator)
                .setSqlFilesCharset(sqlFilesCharset)
                .setRepositoryNameSuffix(repositoryNameSuffix)
                .setRepositoryGenerateInterface(repositoryGenerateInterface)
                .setRepositorySqlStatements(repositorySqlStatements)
                .setClassGeneratedAnnotation(classGeneratedAnnotation)
                .setFieldGeneratedAnnotation(fieldGeneratedAnnotation)
                .setMethodGeneratedAnnotation(methodGeneratedAnnotation)
                .setGeneratedAnnotationComment(generatedAnnotationComment)
                .setMethodBatchPrefix(methodBatchPrefix)
                .setMethodBatchSuffix(methodBatchSuffix)
                .setMethodRxJavaPrefix(methodRxJavaPrefix)
                .setMethodRxJavaSuffix(methodRxJavaSuffix)
                .setMethodStreamPrefix(methodStreamPrefix)
                .setMethodStreamSuffix(methodStreamSuffix)
                .setMethodEagerName(methodEagerName)
                .setMethodLazyName(methodLazyName)
                .setMethodCatchAndRethrow(methodCatchAndRethrow)
                .setGenerateStandardApi(methodStandardApi)
                .setGenerateBatchApi(methodBatchApi)
                .setGenerateStreamEagerApi(parsedJavaVersion > 7 ? methodStreamEagerApi : false)
                .setGenerateStreamLazyApi(parsedJavaVersion > 7 ? methodStreamLazyApi : false)
                .setAllowedReadPrefixes(Arrays.asList("select", "read"))
                .setAllowedWritePrefixes(Arrays.asList("insert", "update", "write"))
                .setAllowedCallPrefixes(Arrays.asList("call", "execute"))
                .setValidateMethodNamePrefixes(methodValidateNamePrefixes)
                .setDefaultRowConverter(defaultRowConverter);

        final String utilPackage = basePackageName + "" + utilityPackageName;
        builder.setFlowStateClass(ClassName.get(utilPackage, "flowState"));
        builder.setResultStateClass(ClassName.get(utilPackage, "resultState"));
        builder.setResultRowClass(ClassName.get(utilPackage, "resultRow"));

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias("resultRow");
        toResultRow.setResultType(utilPackage + "resultRow");
        toResultRow.setConverterType(
                basePackageName
                        + ""
                        + converterPackageName
                        + "ToResultRowConverter");
        resultRowConverters.add(toResultRow);
        builder.setResultRowConverters(resultRowConverters);

        return builder.build();
    }

    private boolean isRxJava2(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, rxJavaGroupId, rxJavaArtifactId);
    }

    private boolean isLog4J(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, log4jGroupId, log4jArtifactId);
    }

    private boolean isSlf4j(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, slf4jGroupId, slf4jArtifactId);
    }

    private static boolean matchesGroupAndArtifact(
            final Dependency dependency,
            final String groupId,
            final String artifactId) {
        return groupId.equals(dependency.getGroupId())
                && artifactId.equals(dependency.getArtifactId());
    }

}
