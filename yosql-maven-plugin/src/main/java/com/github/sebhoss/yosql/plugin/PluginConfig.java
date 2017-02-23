/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.plugin;

import java.io.File;
import java.util.List;
import java.util.function.Supplier;

import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.model.LoggingAPI;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.squareup.javapoet.ClassName;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.logging.Log;

/**
 *
 *
 */
@Named
@Singleton
public class PluginConfig {

    private FileSet                  sqlFiles;
    private File                     outputBaseDirectory;
    private String                   repositorySqlStatements;
    private boolean                  generateBatchApi;
    private boolean                  generateRxJavaApi;
    private String                   methodBatchPrefix;
    private boolean                  generateStreamEagerApi;
    private boolean                  generateStreamLazyApi;
    private String                   methodStreamPrefix;
    private String                   methodStreamSuffix;
    private String                   methodRxJavaPrefix;
    private String                   methodRxJavaSuffix;
    private String                   repositoryNameSuffix;
    private String                   methodLazyName;
    private String                   methodEagerName;
    private boolean                  generateStandardApi;
    private String                   utilityPackageName;
    private String                   converterPackageName;
    private String                   basePackageName;
    private Supplier<Log>            logSupplier;
    private String                   methodBatchSuffix;
    private String[]                 allowedWritePrefixes;
    private String[]                 allowedReadPrefixes;
    private boolean                  validateMethodNamePrefixes;
    private String                   sqlStatementSeparator;
    private String                   sqlFilesCharset;
    private LoggingAPI               loggingApi;
    private boolean                  methodCatchAndRethrow;
    private ClassName                flowStateClass;
    private ClassName                resultStateClass;
    private ClassName                resultRowClass;
    private List<ResultRowConverter> resultRowConverters;
    private String                   defaultRowConverter;
    private boolean                  classGeneratedAnnotation;
    private boolean                  fieldGeneratedAnnotation;
    private boolean                  methodGeneratedAnnotation;
    private String[]                 allowedCallPrefixes;
    private String                   generatedAnnotationComment;
    private boolean                  repositoryGenerateInterface;

    /**
     * @return the outputBaseDirectory
     */
    public File getOutputBaseDirectory() {
        return outputBaseDirectory;
    }

    /**
     * @param outputBaseDirectory
     *            the outputBaseDirectory to set
     */
    public void setOutputBaseDirectory(final File outputBaseDirectory) {
        this.outputBaseDirectory = outputBaseDirectory;
    }

    /**
     * @return How SQL statements are to be included in generated repositories.
     */
    public String getRepositorySqlStatements() {
        return repositorySqlStatements;
    }

    /**
     * @param repositorySqlStatements
     *            How SQL statements are to be included in generated repositories.
     */
    public void setRepositorySqlStatements(final String repositorySqlStatements) {
        this.repositorySqlStatements = repositorySqlStatements;
    }

    /**
     * @return the generateBatchApi
     */
    public boolean isGenerateBatchApi() {
        return generateBatchApi;
    }

    /**
     * @param generateBatchApi
     *            the generateBatchApi to set
     */
    public void setGenerateBatchApi(final boolean generateBatchApi) {
        this.generateBatchApi = generateBatchApi;
    }

    /**
     * @return the generateRxJavaApi
     */
    public boolean isGenerateRxJavaApi() {
        return generateRxJavaApi;
    }

    /**
     * @param generateRxJavaApi
     *            the generateRxJavaApi to set
     */
    public void setGenerateRxJavaApi(final boolean generateRxJavaApi) {
        this.generateRxJavaApi = generateRxJavaApi;
    }

    /**
     * @return the batch prefix
     */
    public String getMethodBatchPrefix() {
        return methodBatchPrefix;
    }

    /**
     * @param methodBatchPrefix
     *            the batch prefix to set
     */
    public void setMethodBatchPrefix(final String methodBatchPrefix) {
        this.methodBatchPrefix = methodBatchPrefix;
    }

    /**
     * @return the method stream prefix
     */
    public String getMethodStreamPrefix() {
        return methodStreamPrefix;
    }

    /**
     * @param methodStreamPrefix
     *            the method stream prefix to set
     */
    public void setMethodStreamPrefix(final String methodStreamPrefix) {
        this.methodStreamPrefix = methodStreamPrefix;
    }

    /**
     * @return the method stream suffix
     */
    public String getMethodStreamSuffix() {
        return methodStreamSuffix;
    }

    /**
     * @param methodStreamSuffix
     *            the method stream suffix to set
     */
    public void setMethodStreamSuffix(final String methodStreamSuffix) {
        this.methodStreamSuffix = methodStreamSuffix;
    }

    /**
     * @return the RxJava prefix
     */
    public String getMethodRxJavaPrefix() {
        return methodRxJavaPrefix;
    }

    /**
     * @param methodRxJavaPrefix
     *            the RxJava prefix to set
     */
    public void setMethodRxJavaPrefix(final String methodRxJavaPrefix) {
        this.methodRxJavaPrefix = methodRxJavaPrefix;
    }

    /**
     * @return the method RxJava suffix
     */
    public String getMethodRxJavaSuffix() {
        return methodRxJavaSuffix;
    }

    /**
     * @param methodRxJavaSuffix
     *            the method RxJava suffix to set
     */
    public void setMethodRxJavaSuffix(final String methodRxJavaSuffix) {
        this.methodRxJavaSuffix = methodRxJavaSuffix;
    }

    /**
     * @return the repositoryNameSuffix
     */
    public String getRepositoryNameSuffix() {
        return repositoryNameSuffix;
    }

    /**
     * @param repositoryNameSuffix
     *            the repositoryNameSuffix to set
     */
    public void setRepositoryNameSuffix(final String repositoryNameSuffix) {
        this.repositoryNameSuffix = repositoryNameSuffix;
    }

    /**
     * @return the generateStreamEagerApi
     */
    public boolean isGenerateStreamEagerApi() {
        return generateStreamEagerApi;
    }

    /**
     * @param generateStreamEagerApi
     *            the generateStreamEagerApi to set
     */
    public void setGenerateStreamEagerApi(final boolean generateStreamEagerApi) {
        this.generateStreamEagerApi = generateStreamEagerApi;
    }

    /**
     * @return the generateStreamLazyApi
     */
    public boolean isGenerateStreamLazyApi() {
        return generateStreamLazyApi;
    }

    /**
     * @param generateStreamLazyApi
     *            the generateStreamLazyApi to set
     */
    public void setGenerateStreamLazyApi(final boolean generateStreamLazyApi) {
        this.generateStreamLazyApi = generateStreamLazyApi;
    }

    /**
     * @return the method lazy name
     */
    public String getMethodLazyName() {
        return methodLazyName;
    }

    /**
     * @param methodLazyName
     *            the method lazy name to set
     */
    public void setMethodLazyName(final String methodLazyName) {
        this.methodLazyName = methodLazyName;
    }

    /**
     * @return the method eager name
     */
    public String getMethodEagerName() {
        return methodEagerName;
    }

    /**
     * @param methodEagerName
     *            the method eager name to set
     */
    public void setMethodEagerName(final String methodEagerName) {
        this.methodEagerName = methodEagerName;
    }

    /**
     * @return the sqlFiles
     */
    public FileSet getSqlFiles() {
        return sqlFiles;
    }

    /**
     * @param generateStandardApi
     *            Whether to create the standard API or not.
     */
    public void setGenerateStandardApi(final boolean generateStandardApi) {
        this.generateStandardApi = generateStandardApi;
    }

    /**
     * @return the generateStandardApi
     */
    public boolean isGenerateStandardApi() {
        return generateStandardApi;
    }

    /**
     * @return the utilityPackageName
     */
    public String getUtilityPackageName() {
        return utilityPackageName;
    }

    /**
     * @param utilityPackageName
     *            the utilityPackageName to set
     */
    public void setUtilityPackageName(final String utilityPackageName) {
        this.utilityPackageName = utilityPackageName;
    }

    /**
     * @return the converterPackageName
     */
    public String getConverterPackageName() {
        return converterPackageName;
    }

    /**
     * @param converterPackageName
     *            the converterPackageName to set
     */
    public void setConverterPackageName(final String converterPackageName) {
        this.converterPackageName = converterPackageName;
    }

    /**
     * @return the basePackageName
     */
    public String getBasePackageName() {
        return basePackageName;
    }

    /**
     * @param basePackageName
     *            the basePackageName to set
     */
    public void setBasePackageName(final String basePackageName) {
        this.basePackageName = basePackageName;
    }

    /**
     * @param logSupplier
     *            The log supplier to use.
     */
    public void setLogger(final Supplier<Log> logSupplier) {
        this.logSupplier = logSupplier;

    }

    /**
     * @return The log supplier
     */
    public Log getLogger() {
        return logSupplier.get();
    }

    /**
     * @param methodBatchSuffix
     *            The batch method name suffix
     */
    public void setMethodBatchSuffix(final String methodBatchSuffix) {
        this.methodBatchSuffix = methodBatchSuffix;
    }

    /**
     * @return The batch method name suffix
     */
    public String getMethodBatchSuffix() {
        return methodBatchSuffix;
    }

    /**
     * @return the allowedWritePrefixes
     */
    public String[] getAllowedWritePrefixes() {
        return allowedWritePrefixes;
    }

    /**
     * @param allowedWritePrefixes
     *            the allowedWritePrefixes to set
     */
    public void setAllowedWritePrefixes(final String[] allowedWritePrefixes) {
        this.allowedWritePrefixes = allowedWritePrefixes;
    }

    /**
     * @return the allowedReadPrefixes
     */
    public String[] getAllowedReadPrefixes() {
        return allowedReadPrefixes;
    }

    /**
     * @param allowedReadPrefixes
     *            the allowedReadPrefixes to set
     */
    public void setAllowedReadPrefixes(final String[] allowedReadPrefixes) {
        this.allowedReadPrefixes = allowedReadPrefixes;
    }

    /**
     * @return the validateMethodNamePrefixes
     */
    public boolean shouldValidateMethodNamePrefixes() {
        return validateMethodNamePrefixes;
    }

    /**
     * @param validateMethodNamePrefixes
     *            the validateMethodNamePrefixes to set
     */
    public void setValidateMethodNamePrefixes(final boolean validateMethodNamePrefixes) {
        this.validateMethodNamePrefixes = validateMethodNamePrefixes;
    }

    /**
     * @return The SQL statement separator
     */
    public String getSqlStatementSeparator() {
        return sqlStatementSeparator;
    }

    /**
     * @param sqlStatementSeperator
     *            The SQL statement separator
     */
    public void setSqlStatementSeparator(final String sqlStatementSeperator) {
        sqlStatementSeparator = sqlStatementSeperator;
    }

    /**
     * @return The SQL file charset to use.
     */
    public String getSqlFilesCharset() {
        return sqlFilesCharset;
    }

    /**
     * @param sqlFilesCharset
     *            The SQL file charset to use.
     */
    public void setSqlFilesCharset(final String sqlFilesCharset) {
        this.sqlFilesCharset = sqlFilesCharset;
    }

    /**
     * @param loggingApi
     *            The logging API to use.
     */
    public void setLoggingApi(final LoggingAPI loggingApi) {
        this.loggingApi = loggingApi;
    }

    /**
     * @return The logging API to use.
     */
    public LoggingAPI getLoggingApi() {
        return loggingApi;
    }

    /**
     * @return Whether log statements should be generated
     */
    public boolean shouldLog() {
        return LoggingAPI.NONE != loggingApi;
    }

    /**
     * @param methodCatchAndRethrow
     *            Whether methods should catch & rethrow
     */
    public void setMethodCatchAndRethrow(final boolean methodCatchAndRethrow) {
        this.methodCatchAndRethrow = methodCatchAndRethrow;
    }

    /**
     * @return Whether methods should catch & rethrow
     */
    public boolean isMethodCatchAndRethrow() {
        return methodCatchAndRethrow;
    }

    /**
     * @param flowStateClass
     *            The name of the flow-state class
     */
    public void setFlowStateClass(final ClassName flowStateClass) {
        this.flowStateClass = flowStateClass;
    }

    /**
     * @return The name of the flow-state class
     */
    public ClassName getFlowStateClass() {
        return flowStateClass;
    }

    /**
     * @param resultStateClass
     *            The name of the result-state class
     */
    public void setResultStateClass(final ClassName resultStateClass) {
        this.resultStateClass = resultStateClass;
    }

    /**
     * @return The name of the result-state class
     */
    public ClassName getResultStateClass() {
        return resultStateClass;
    }

    /**
     * @param resultRowClass
     *            The name of the result-row class
     */
    public void setResultRowClass(final ClassName resultRowClass) {
        this.resultRowClass = resultRowClass;
    }

    /**
     * @return The name of the result-row class
     */
    public ClassName getResultRowClass() {
        return resultRowClass;
    }

    /**
     * @return the resultRowConverters
     */
    public List<ResultRowConverter> getResultRowConverters() {
        return resultRowConverters;
    }

    /**
     * @param resultRowConverters
     *            the resultRowConverters to set.
     */
    public void setResultRowConverters(final List<ResultRowConverter> resultRowConverters) {
        this.resultRowConverters = resultRowConverters;
    }

    /**
     * @param defaultRowConverter
     *            The default result row converter.
     */
    public void setDefaultRowConverter(final String defaultRowConverter) {
        this.defaultRowConverter = defaultRowConverter;
    }

    /**
     * @return The default result row converter.
     */
    public String getDefaultRowConverter() {
        return defaultRowConverter;
    }

    /**
     * @return Should @Generated annotations be generated for classes.
     */
    public boolean isClassGeneratedAnnotation() {
        return classGeneratedAnnotation;
    }

    /**
     * @return Should @Generated annotations be generated for fields.
     */
    public boolean isFieldGeneratedAnnotation() {
        return fieldGeneratedAnnotation;
    }

    /**
     * @return Should @Generated annotations be generated for methods.
     */
    public boolean isMethodGeneratedAnnotation() {
        return methodGeneratedAnnotation;
    }

    /**
     * @param classGeneratedAnnotation
     *            Should @Generated annotations be generated for classes.
     */
    public void setClassGeneratedAnnotation(final boolean classGeneratedAnnotation) {
        this.classGeneratedAnnotation = classGeneratedAnnotation;
    }

    /**
     * @param fieldGeneratedAnnotation
     *            Should @Generated annotations be generated for fields.
     */
    public void setFieldGeneratedAnnotation(final boolean fieldGeneratedAnnotation) {
        this.fieldGeneratedAnnotation = fieldGeneratedAnnotation;
    }

    /**
     * @param methodGeneratedAnnotation
     *            Should @Generated annotations be generated for methods.
     */
    public void setMethodGeneratedAnnotation(final boolean methodGeneratedAnnotation) {
        this.methodGeneratedAnnotation = methodGeneratedAnnotation;
    }

    /**
     * @return Allowed name prefixes for calling statements.
     */
    public String[] getAllowedCallPrefixes() {
        return allowedCallPrefixes;
    }

    /**
     * @param allowedCallPrefixes
     *            Allowed name prefixes for calling statements.
     */
    public void setAllowedCallPrefixes(final String[] allowedCallPrefixes) {
        this.allowedCallPrefixes = allowedCallPrefixes;
    }

    /**
     * @param generatedAnnotationComment
     *            The comment used for @Generated annotations.
     */
    public void setGeneratedAnnotationComment(final String generatedAnnotationComment) {
        this.generatedAnnotationComment = generatedAnnotationComment;
    }

    /**
     * @return The comment used for @Generated annotations.
     */
    public String getGeneratedAnnotationComment() {
        return generatedAnnotationComment;
    }

    /**
     * @param repositoryGenerateInterface
     *            Whether an interface should be generated for each repository.
     */
    public void setRepositoryGenerateInterface(final boolean repositoryGenerateInterface) {
        this.repositoryGenerateInterface = repositoryGenerateInterface;
    }

    /**
     * @return Whether an interface should be generated for each repository.
     */
    public boolean isRepositoryGenerateInterface() {
        return repositoryGenerateInterface;
    }

}
