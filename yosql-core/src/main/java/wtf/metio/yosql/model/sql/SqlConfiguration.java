/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.model.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public class SqlConfiguration {

    private String name;
    private String repository;
    private SqlType type;
    private List<SqlParameter> parameters;
    private ResultRowConverter resultRowConverter;
    private boolean methodStandardApi;
    private boolean generateStandardApiOverwritten;
    private boolean methodBatchApi;
    private boolean generateBatchApiOverwritten;
    private boolean methodRxJavaApi;
    private boolean generateRxJavaApiOverwritten;
    private boolean methodStreamEagerApi;
    private boolean generateStreamEagerApiOverwritten;
    private boolean methodStreamLazyApi;
    private boolean generateStreamLazyApiOverwritten;
    private String methodBatchPrefix;
    private String methodBatchSuffix;
    private String methodStreamPrefix;
    private String methodStreamSuffix;
    private String methodRxJavaPrefix;
    private String methodRxJavaSuffix;
    private String methodLazyName;
    private String methodEagerName;
    private boolean methodCatchAndRethrow;
    private boolean methodCatchAndRethrowOverwritten;
    private String vendor;
    private ReturningMode returningMode;

    public String getFlowableName() {
        return joinMethodNameParts(methodRxJavaPrefix, name, methodRxJavaSuffix);
    }

    public String getBatchName() {
        return joinMethodNameParts(methodBatchPrefix, name, methodBatchSuffix);
    }

    public String getStreamLazyName() {
        return joinMethodNameParts(methodStreamPrefix, name, methodStreamSuffix, methodLazyName);
    }

    public String getStreamEagerName() {
        return joinMethodNameParts(methodStreamPrefix, name, methodStreamSuffix, methodEagerName);
    }

    private static String joinMethodNameParts(final String... strings) {
        final var part = new AtomicInteger();
        return Arrays.stream(strings)
                .filter(Objects::nonNull)
                .map(String::strip)
                .filter(not(String::isBlank))
                .map(string -> part.getAndIncrement() == 0
                        ? string
                        : string.substring(0, 1).toUpperCase() + string.substring(1))
                .collect(Collectors.joining());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the methodStandardApi
     */
    public boolean isMethodStandardApi() {
        return methodStandardApi;
    }

    /**
     * @param methodStandardApi the methodStandardApi to set
     */
    public void setMethodStandardApi(final boolean methodStandardApi) {
        this.methodStandardApi = methodStandardApi;
        generateStandardApiOverwritten = true;
    }

    public boolean isMethodBatchApi() {
        return methodBatchApi;
    }

    public void setMethodBatchApi(final boolean methodBatchApi) {
        this.methodBatchApi = methodBatchApi;
        generateBatchApiOverwritten = true;
    }

    public String getMethodBatchPrefix() {
        return methodBatchPrefix;
    }

    public void setMethodBatchPrefix(final String methodBatchPrefix) {
        this.methodBatchPrefix = methodBatchPrefix;
    }

    /**
     * @return the method batch suffix
     */
    public String getMethodBatchSuffix() {
        return methodBatchSuffix;
    }

    /**
     * @param methodBatchSuffix the method batch suffix to set
     */
    public void setMethodBatchSuffix(final String methodBatchSuffix) {
        this.methodBatchSuffix = methodBatchSuffix;
    }

    /**
     * @return the methodRxJavaPrefix
     */
    public String getMethodReactivePrefix() {
        return methodRxJavaPrefix;
    }

    /**
     * @param methodRxJavaPrefix the methodRxJavaPrefix to set
     */
    public void setMethodReactivePrefix(final String methodRxJavaPrefix) {
        this.methodRxJavaPrefix = methodRxJavaPrefix;
    }

    /**
     * @return the methodRxJavaSuffix
     */
    public String getMethodReactiveSuffix() {
        return methodRxJavaSuffix;
    }

    /**
     * @param methodRxJavaSuffix the methodRxJavaSuffix to set
     */
    public void setMethodReactiveSuffix(final String methodRxJavaSuffix) {
        this.methodRxJavaSuffix = methodRxJavaSuffix;
    }

    /**
     * @return the methodStreamEagerApi
     */
    public boolean isMethodStreamEagerApi() {
        return methodStreamEagerApi;
    }

    /**
     * @param methodStreamEagerApi the methodStreamEagerApi to set
     */
    public void setMethodStreamEagerApi(final boolean methodStreamEagerApi) {
        this.methodStreamEagerApi = methodStreamEagerApi;
        generateStreamEagerApiOverwritten = true;
    }

    /**
     * @return the methodStreamLazyApi
     */
    public boolean isMethodStreamLazyApi() {
        return methodStreamLazyApi;
    }

    /**
     * @param methodStreamLazyApi the methodStreamLazyApi to set
     */
    public void setMethodStreamLazyApi(final boolean methodStreamLazyApi) {
        this.methodStreamLazyApi = methodStreamLazyApi;
        generateStreamLazyApiOverwritten = true;
    }

    public String getMethodStreamPrefix() {
        return methodStreamPrefix;
    }

    public void setMethodStreamPrefix(final String methodStreamPrefix) {
        this.methodStreamPrefix = methodStreamPrefix;
    }

    /**
     * @return the methodStreamSuffix
     */
    public String getMethodStreamSuffix() {
        return methodStreamSuffix;
    }

    /**
     * @param methodStreamSuffix the methodStreamSuffix to set
     */
    public void setMethodStreamSuffix(final String methodStreamSuffix) {
        this.methodStreamSuffix = methodStreamSuffix;
    }

    public List<SqlParameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<SqlParameter> parameters) {
        this.parameters = parameters;
    }

    public ResultRowConverter getResultRowConverter() {
        return resultRowConverter;
    }

    public void setResultRowConverter(final ResultRowConverter resultRowConverter) {
        this.resultRowConverter = resultRowConverter;
    }

    public boolean hasRepository() {
        return repository != null && !repository.isBlank();
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(final String repository) {
        this.repository = repository;
    }

    /**
     * @return the methodLazyName
     */
    public String getMethodLazyName() {
        return methodLazyName;
    }

    /**
     * @param methodLazyName the methodLazyName to set
     */
    public void setMethodLazyName(final String methodLazyName) {
        this.methodLazyName = methodLazyName;
    }

    /**
     * @return the methodEagerName
     */
    public String getMethodEagerName() {
        return methodEagerName;
    }

    /**
     * @param methodEagerName the methodEagerName to set
     */
    public void setMethodEagerName(final String methodEagerName) {
        this.methodEagerName = methodEagerName;
    }

    /**
     * @return <code>true</code> if 'single' was not overwritten.
     */
    public boolean shouldUsePluginStandardConfig() {
        return !generateStandardApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'generateBatchApi' was not overwritten and the plugin configuration should be used.
     */
    public boolean shouldUsePluginBatchConfig() {
        return !generateBatchApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamEager' was not overwritten.
     */
    public boolean shouldUsePluginStreamEagerConfig() {
        return !generateStreamEagerApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamLazy' was not overwritten.
     */
    public boolean shouldUsePluginStreamLazyConfig() {
        return !generateStreamLazyApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'generateRxJavaApi' was not overwritten.
     */
    public boolean shouldUsePluginRxJavaConfig() {
        return !generateRxJavaApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'parameters' has at least one parameter.
     */
    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    /**
     * @return the type
     */
    public SqlType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(final SqlType type) {
        this.type = type;
    }

    /**
     * @return the methodRxJavaApi
     */
    public boolean isMethodRxJavaApi() {
        return methodRxJavaApi;
    }

    /**
     * @param methodRxJavaApi the methodRxJavaApi to set
     */
    public void setMethodRxJavaApi(final boolean methodRxJavaApi) {
        this.methodRxJavaApi = methodRxJavaApi;
        generateRxJavaApiOverwritten = true;
    }

    /**
     * @return the methodCatchAndRethrow
     */
    public boolean isMethodCatchAndRethrow() {
        return methodCatchAndRethrow;
    }

    /**
     * @param methodCatchAndRethrow the methodCatchAndRethrow to set
     */
    public void setMethodCatchAndRethrow(final boolean methodCatchAndRethrow) {
        this.methodCatchAndRethrow = methodCatchAndRethrow;
        methodCatchAndRethrowOverwritten = true;
    }

    /**
     * @return <code>true</code> if 'streamLazy' was not overwritten.
     */
    public boolean shouldUsePluginCatchAndRethrowConfig() {
        return !methodCatchAndRethrowOverwritten;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor the vendor to set
     */
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    public ReturningMode getReturningMode() {
        return returningMode;
    }

    public void setReturningMode(final ReturningMode returningMode) {
        this.returningMode = returningMode;
    }

    public static SqlConfiguration fromStatements(final List<SqlStatement> statements) {
        final var configuration = new SqlConfiguration();
        statements.forEach(configuration::merge);
        return configuration;
    }

    public void merge(final SqlStatement statement) {
        final var other = statement.getConfiguration();
        name = name != null ? name : other.name;
        repository = repository != null ? repository : other.repository;
        type = type != null ? type : other.type;
        parameters = mergeParameters(other.parameters);
        resultRowConverter = resultRowConverter != null ? resultRowConverter : other.resultRowConverter;
        methodStandardApi = methodStandardApi || other.methodStandardApi;
        generateStandardApiOverwritten = generateStandardApiOverwritten || other.generateStandardApiOverwritten;
        methodBatchApi = methodBatchApi || other.methodBatchApi;
        generateBatchApiOverwritten = generateBatchApiOverwritten || other.generateBatchApiOverwritten;
        methodRxJavaApi = methodRxJavaApi || other.methodRxJavaApi;
        generateRxJavaApiOverwritten = generateRxJavaApiOverwritten || other.generateRxJavaApiOverwritten;
        methodStreamEagerApi = methodStreamEagerApi || other.methodStreamEagerApi;
        generateStreamEagerApiOverwritten = generateStreamEagerApiOverwritten || other.generateStreamEagerApiOverwritten;
        methodStreamLazyApi = methodStreamLazyApi || other.methodStreamLazyApi;
        generateStreamLazyApiOverwritten = generateStreamLazyApiOverwritten || other.generateStreamLazyApiOverwritten;
        methodBatchPrefix = methodBatchPrefix != null ? methodBatchPrefix : other.methodBatchPrefix;
        methodBatchSuffix = methodBatchSuffix != null ? methodBatchSuffix : other.methodBatchSuffix;
        methodStreamPrefix = methodStreamPrefix != null ? methodStreamPrefix : other.methodStreamPrefix;
        methodStreamSuffix = methodStreamSuffix != null ? methodStreamSuffix : other.methodStreamSuffix;
        methodRxJavaPrefix = methodRxJavaPrefix != null ? methodRxJavaPrefix : other.methodRxJavaPrefix;
        methodRxJavaSuffix = methodRxJavaSuffix != null ? methodRxJavaSuffix : other.methodRxJavaSuffix;
        methodLazyName = methodLazyName != null ? methodLazyName : other.methodLazyName;
        methodEagerName = methodEagerName != null ? methodEagerName : other.methodEagerName;
        methodCatchAndRethrow = methodCatchAndRethrow || other.methodCatchAndRethrow;
        methodCatchAndRethrowOverwritten = methodCatchAndRethrowOverwritten || other.methodCatchAndRethrowOverwritten;
        vendor = vendor != null ? vendor : other.vendor;
    }

    private List<SqlParameter> mergeParameters(final List<SqlParameter> otherParameters) {
        if (parameters == null || parameters.isEmpty()) {
            return otherParameters;
        }
        return parameters.stream()
                .map(param -> otherParameters.stream()
                        .filter(op -> param.name().equals(op.name()))
                        .findFirst()
                        .map(other -> SqlParameter.builder()
                                .setName(param.name())
                                .setType(Object.class.getName().equals(param.type()) ? other.type() : param.type())
                                .setConverter((param.converter() == null || param.converter().isBlank()) ? other.converter() : param.converter())
                                .setIndices(param.indices() == null ? other.indices() : param.indices())
                                .build())
                        .orElse(SqlParameter.copy(param)))
                .collect(Collectors.toList());
    }

}
