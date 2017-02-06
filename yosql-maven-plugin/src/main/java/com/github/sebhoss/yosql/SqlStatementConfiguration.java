package com.github.sebhoss.yosql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.github.sebhoss.yosql.generator.TypicalParameters;
import com.squareup.javapoet.ParameterSpec;

public class SqlStatementConfiguration {

    private String             name;
    private String             repository;
    private SqlStatementType   type;
    private List<SqlParameter> parameters = new ArrayList<>();
    private ResultRowConverter resultConverter;
    private boolean            generateStandardApi;
    private boolean            generateStandardApiOverwritten;
    private boolean            generateBatchApi;
    private boolean            generateBatchApiOverwritten;
    private boolean            generateRxJavaApi;
    private boolean            generateRxJavaApiOverwritten;
    private boolean            generateStreamEagerApi;
    private boolean            generateStreamEagerApiOverwritten;
    private boolean            generateStreamLazyApi;
    private boolean            generateStreamLazyApiOverwritten;
    private String             methodBatchPrefix;
    private String             methodBatchSuffix;
    private String             methodStreamPrefix;
    private String             methodStreamSuffix;
    private String             methodRxJavaPrefix;
    private String             methodRxJavaSuffix;
    private String             methodLazyName;
    private String             methodEagerName;
    private boolean            methodCatchAndRethrow;
    private boolean            methodCatchAndRethrowOverwritten;
    private String             vendor;

    public Iterable<ParameterSpec> getParameterSpecs() {
        return getParameterSpecs(TypicalParameters::ofSqlParameter);
    }

    public Iterable<ParameterSpec> getBatchParameterSpecs() {
        return getParameterSpecs(TypicalParameters::batchOfSqlParameter);
    }

    private Iterable<ParameterSpec> getParameterSpecs(final Function<SqlParameter, ParameterSpec> asParameter) {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(asParameter)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public String getFlowableName() {
        return join(methodRxJavaPrefix, name, methodRxJavaSuffix);
    }

    public String getBatchName() {
        return join(methodBatchPrefix, name, methodBatchSuffix);
    }

    public String getStreamLazyName() {
        return join(methodStreamPrefix, name, methodStreamSuffix, methodLazyName);
    }

    public String getStreamEagerName() {
        return join(methodStreamPrefix, name, methodStreamSuffix, methodEagerName);
    }

    private String join(final String... strings) {
        final AtomicInteger hits = new AtomicInteger(0);
        return Arrays.stream(strings)
                .filter(Objects::nonNull)
                .map(s -> hits.getAndIncrement() == 0 ? s : s.substring(0, 1).toUpperCase()
                        + s.substring(1, s.length()))
                .collect(Collectors.joining());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the generateStandardApi
     */
    public boolean isGenerateStandardApi() {
        return generateStandardApi;
    }

    /**
     * @param generateStandardApi
     *            the generateStandardApi to set
     */
    public void setGenerateStandardApi(final boolean generateStandardApi) {
        this.generateStandardApi = generateStandardApi;
        generateStandardApiOverwritten = true;
    }

    public boolean isGenerateBatchApi() {
        return generateBatchApi;
    }

    public void setGenerateBatchApi(final boolean generateBatchApi) {
        this.generateBatchApi = generateBatchApi;
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
     * @param methodBatchSuffix
     *            the method batch suffix to set
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
     * @param methodRxJavaPrefix
     *            the methodRxJavaPrefix to set
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
     * @param methodRxJavaSuffix
     *            the methodRxJavaSuffix to set
     */
    public void setMethodReactiveSuffix(final String methodRxJavaSuffix) {
        this.methodRxJavaSuffix = methodRxJavaSuffix;
    }

    /**
     * @return the streamEager
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
        generateStreamEagerApiOverwritten = true;
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
     * @param methodStreamSuffix
     *            the methodStreamSuffix to set
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

    public ResultRowConverter getResultConverter() {
        return resultConverter;
    }

    public void setResultConverter(final ResultRowConverter resultConverter) {
        this.resultConverter = resultConverter;
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
     * @param methodLazyName
     *            the methodLazyName to set
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
     * @param methodEagerName
     *            the methodEagerName to set
     */
    public void setMethodEagerName(final String methodEagerName) {
        this.methodEagerName = methodEagerName;
    }

    /**
     * @return <code>true</code> if 'single' was not overwritten.
     */
    protected boolean isUsingPluginSingleConfig() {
        return !generateStandardApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'generateBatchApi' was not overwritten and
     *         the plugin configuration should be used.
     */
    protected boolean isUsingPluginBatchConfig() {
        return !generateBatchApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamEager' was not overwritten.
     */
    protected boolean isUsingPluginStreamEagerConfig() {
        return !generateStreamEagerApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamLazy' was not overwritten.
     */
    protected boolean isUsingPluginStreamLazyConfig() {
        return !generateStreamLazyApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'generateRxJavaApi' was not overwritten.
     */
    public boolean isUsingPluginRxJavaConfig() {
        return !generateRxJavaApiOverwritten;
    }

    /**
     * @return <code>true</code> if 'parameters' has at least one parameter.
     */
    protected boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    /**
     * @return the type
     */
    public SqlStatementType getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(final SqlStatementType type) {
        this.type = type;
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
        generateRxJavaApiOverwritten = true;
    }

    /**
     * @return the methodCatchAndRethrow
     */
    public boolean isMethodCatchAndRethrow() {
        return methodCatchAndRethrow;
    }

    /**
     * @param methodCatchAndRethrow
     *            the methodCatchAndRethrow to set
     */
    public void setMethodCatchAndRethrow(final boolean methodCatchAndRethrow) {
        this.methodCatchAndRethrow = methodCatchAndRethrow;
        methodCatchAndRethrowOverwritten = true;
    }

    /**
     * @return <code>true</code> if 'streamLazy' was not overwritten.
     */
    protected boolean isUsingPluginCatchAndRethrowConfig() {
        return !methodCatchAndRethrowOverwritten;
    }

    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * @param vendor
     *            the vendor to set
     */
    public void setVendor(final String vendor) {
        this.vendor = vendor;
    }

    public void merge(final SqlStatement statement) {
        final SqlStatementConfiguration other = statement.getConfiguration();
        name = name != null ? name : other.name;
        repository = repository != null ? repository : other.repository;
        type = type != null ? type : other.type;
        parameters = mergeParameters(other.parameters);
        resultConverter = resultConverter != null ? resultConverter : other.resultConverter;
        generateStandardApi = generateStandardApi || other.generateStandardApi;
        generateStandardApiOverwritten = generateStandardApiOverwritten || other.generateStandardApiOverwritten;
        generateBatchApi = generateBatchApi || other.generateBatchApi;
        generateBatchApiOverwritten = generateBatchApiOverwritten || other.generateBatchApiOverwritten;
        generateRxJavaApi = generateRxJavaApi || other.generateRxJavaApi;
        generateRxJavaApiOverwritten = generateRxJavaApiOverwritten || other.generateRxJavaApiOverwritten;
        generateStreamEagerApi = generateStreamEagerApi || other.generateStreamEagerApi;
        generateStreamEagerApiOverwritten = generateStreamEagerApiOverwritten
                || other.generateStreamEagerApiOverwritten;
        generateStreamLazyApi = generateStreamLazyApi || other.generateStreamLazyApi;
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
        final List<SqlParameter> params = parameters != null && !parameters.isEmpty() ? parameters : otherParameters;
        params.stream()
                .forEach(param -> {
                    otherParameters.stream()
                            .filter(op -> param.getName().equals(op.getName()))
                            .findFirst()
                            .ifPresent(otherParam -> {
                                if (param.getName() == null
                                        || !param.getName().isEmpty()
                                        || "java.lang.Object".equals(param.getType())) {
                                    param.setType(otherParam.getType());
                                }
                                if (param.getConverter() == null || !param.getConverter().isEmpty()) {
                                    param.setConverter(otherParam.getConverter());
                                }
                            });
                });
        return params;
    }

}
