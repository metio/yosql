package com.github.sebhoss.yosql;

import java.io.File;
import java.util.function.Supplier;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.logging.Log;

@Named
@Singleton
public class PluginRuntimeConfig {

    private FileSet       sqlFiles;
    private File          outputBaseDirectory;
    private boolean       compileInline;
    private boolean       generateBatchApi;
    private boolean       generateRxJavaApi;
    private String        batchPrefix;
    private boolean       generateStreamEagerApi;
    private boolean       generateStreamLazyApi;
    private String        streamPrefix;
    private String        streamSuffix;
    private String        reactivePrefix;
    private String        reactiveSuffix;
    private String        lazyName;
    private String        eagerName;
    private boolean       generateSingleQueryApi;
    private String        utilityPackageName;
    private String        basePackageName;
    private Supplier<Log> logSupplier;
    private String        batchSuffix;
    private String[]      allowedWritePrefixes;
    private String[]      allowedReadPrefixes;
    private boolean       validateMethodNamePrefixes;

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
     * @return the compileInline
     */
    public boolean isCompileInline() {
        return compileInline;
    }

    /**
     * @param compileInline
     *            the compileInline to set
     */
    public void setCompileInline(final boolean compileInline) {
        this.compileInline = compileInline;
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
     * @return the batchPrefix
     */
    public String getBatchPrefix() {
        return batchPrefix;
    }

    /**
     * @param batchPrefix
     *            the batchPrefix to set
     */
    public void setBatchPrefix(final String batchPrefix) {
        this.batchPrefix = batchPrefix;
    }

    /**
     * @return the streamPrefix
     */
    public String getStreamPrefix() {
        return streamPrefix;
    }

    /**
     * @param streamPrefix
     *            the streamPrefix to set
     */
    public void setStreamPrefix(final String streamPrefix) {
        this.streamPrefix = streamPrefix;
    }

    /**
     * @return the streamSuffix
     */
    public String getStreamSuffix() {
        return streamSuffix;
    }

    /**
     * @param streamSuffix
     *            the streamSuffix to set
     */
    public void setStreamSuffix(final String streamSuffix) {
        this.streamSuffix = streamSuffix;
    }

    /**
     * @return the reactivePrefix
     */
    public String getReactivePrefix() {
        return reactivePrefix;
    }

    /**
     * @param reactivePrefix
     *            the reactivePrefix to set
     */
    public void setReactivePrefix(final String reactivePrefix) {
        this.reactivePrefix = reactivePrefix;
    }

    /**
     * @return the reactiveSuffix
     */
    public String getReactiveSuffix() {
        return reactiveSuffix;
    }

    /**
     * @param reactiveSuffix
     *            the reactiveSuffix to set
     */
    public void setReactiveSuffix(final String reactiveSuffix) {
        this.reactiveSuffix = reactiveSuffix;
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
     * @return the lazyName
     */
    public String getLazyName() {
        return lazyName;
    }

    /**
     * @param lazyName
     *            the lazyName to set
     */
    public void setLazyName(final String lazyName) {
        this.lazyName = lazyName;
    }

    /**
     * @return the eagerName
     */
    public String getEagerName() {
        return eagerName;
    }

    /**
     * @param eagerName
     *            the eagerName to set
     */
    public void setEagerName(final String eagerName) {
        this.eagerName = eagerName;
    }

    /**
     * @return the sqlFiles
     */
    public FileSet getSqlFiles() {
        return sqlFiles;
    }

    public void setGenerateSingleQueryApi(final boolean generateSingleQueryApi) {
        this.generateSingleQueryApi = generateSingleQueryApi;
    }

    /**
     * @return the generateSingleQueryApi
     */
    public boolean isGenerateSingleQueryApi() {
        return generateSingleQueryApi;
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
     * @return the basePackageName
     */
    public String getBasePackageName() {
        return basePackageName;
    }

    /**
     * @param basePackageName the basePackageName to set
     */
    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    public void setLogger(final Supplier<Log> logSupplier) {
        this.logSupplier = logSupplier;

    }

    public Log getLogger() {
        return logSupplier.get();
    }

    public void setBatchSuffix(final String batchSuffix) {
        this.batchSuffix = batchSuffix;
    }

    public String getBatchSuffix() {
        return batchSuffix;
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
    public boolean isValidateMethodNamePrefixes() {
        return validateMethodNamePrefixes;
    }

    /**
     * @param validateMethodNamePrefixes
     *            the validateMethodNamePrefixes to set
     */
    public void setValidateMethodNamePrefixes(final boolean validateMethodNamePrefixes) {
        this.validateMethodNamePrefixes = validateMethodNamePrefixes;
    }
}
