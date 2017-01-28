package com.github.sebhoss.yosql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.sebhoss.yosql.generator.TypicalModifiers;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public class SqlStatementConfiguration {

    private String             name;
    private String             upperCaseName;
    private boolean            single;
    private boolean            singleOverwritten;
    private boolean            batch;
    private boolean            batchOverwritten;
    private String             batchPrefix;
    private String             batchSuffix;
    private boolean            streamEager;
    private boolean            streamEagerOverwritten;
    private boolean            streamLazy;
    private boolean            streamLazyOverwritten;
    private String             streamPrefix;
    private String             streamSuffix;
    private List<SqlParameter> parameters = new ArrayList<>();
    private String             resultConverter;
    private String             repository;
    private String             lazyName;
    private String             eagerName;
    private SqlStatementType   type;

    public Iterable<ParameterSpec> getParameterSpecs() {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(param -> ParameterSpec
                                .builder(calculateTypeName(param), param.getName(), TypicalModifiers.PARAMETER)
                                .build())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public Iterable<ParameterSpec> getBatchParameterSpecs() {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(param -> ParameterSpec
                                .builder(calculateBatchTypeName(param), param.getName(), TypicalModifiers.PARAMETER)
                                .build())
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private TypeName calculateTypeName(final SqlParameter param) {
        final String userSpecifiedType = param.getType();
        if (userSpecifiedType.contains(".")) {
            return ClassName.bestGuess(userSpecifiedType);
        } else {
            switch (userSpecifiedType) {
            case "boolean":
                return TypeName.BOOLEAN;
            case "byte":
                return TypeName.BYTE;
            case "short":
                return TypeName.SHORT;
            case "long":
                return TypeName.LONG;
            case "char":
                return TypeName.CHAR;
            case "float":
                return TypeName.FLOAT;
            case "double":
                return TypeName.DOUBLE;
            case "int":
                return TypeName.INT;
            default:
                return TypeName.OBJECT;
            }
        }
    }

    private TypeName calculateBatchTypeName(final SqlParameter param) {
        final TypeName calculateTypeName = calculateTypeName(param);
        return ArrayTypeName.of(calculateTypeName);
    }

    public String getBatchName() {
        return suffixedName(prefixedName(batchPrefix), batchSuffix);
    }

    public String getStreamLazyName() {
        return suffixedName(prefixedName(streamPrefix, "Lazy"), streamSuffix);
    }

    public String getStreamEagerName() {
        return suffixedName(prefixedName(streamPrefix, "Eager"), streamSuffix);
    }

    private String prefixedName(final String prefix) {
        return prefix == null || prefix.isEmpty() ? name : prefix + upperCaseName;
    }

    private String prefixedName(final String prefix1, final String prefix2) {
        return prefix1 == null || prefix1.isEmpty() ? name : prefix1 + prefix2 + upperCaseName;
    }

    private String suffixedName(final String first, final String suffix) {
        return suffix == null || suffix.isEmpty() ? first : first + suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
        upperCaseName = name.substring(0, 1).toUpperCase()
                + name.substring(1, name.length());
    }

    /**
     * @return the single
     */
    public boolean isSingle() {
        return single;
    }

    /**
     * @param single
     *            the single to set
     */
    public void setSingle(final boolean single) {
        this.single = single;
        singleOverwritten = true;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(final boolean batch) {
        this.batch = batch;
        batchOverwritten = true;
    }

    public String getBatchPrefix() {
        return batchPrefix;
    }

    public void setBatchPrefix(final String batchPrefix) {
        this.batchPrefix = batchPrefix;
    }

    /**
     * @return the batchSuffix
     */
    public String getBatchSuffix() {
        return batchSuffix;
    }

    /**
     * @param batchSuffix
     *            the batchSuffix to set
     */
    public void setBatchSuffix(final String batchSuffix) {
        this.batchSuffix = batchSuffix;
    }

    /**
     * @return the streamEager
     */
    public boolean isStreamEager() {
        return streamEager;
    }

    /**
     * @param streamEager
     *            the streamEager to set
     */
    public void setStreamEager(final boolean streamEager) {
        this.streamEager = streamEager;
        streamEagerOverwritten = true;
    }

    /**
     * @return the streamLazy
     */
    public boolean isStreamLazy() {
        return streamLazy;
    }

    /**
     * @param streamLazy
     *            the streamLazy to set
     */
    public void setStreamLazy(final boolean streamLazy) {
        this.streamLazy = streamLazy;
        streamLazyOverwritten = true;
    }

    public String getStreamPrefix() {
        return streamPrefix;
    }

    public void setStreamPrefix(final String streamPrefix) {
        this.streamPrefix = streamPrefix;
    }

    /**
     * @return the streamPostfix
     */
    public String getStreamPostfix() {
        return streamSuffix;
    }

    /**
     * @param streamPostfix
     *            the streamPostfix to set
     */
    public void setStreamPostfix(final String streamPostfix) {
        streamSuffix = streamPostfix;
    }

    public List<SqlParameter> getParameters() {
        return parameters;
    }

    public void setParameters(final List<SqlParameter> parameters) {
        this.parameters = parameters;
    }

    public String getResultConverter() {
        return resultConverter;
    }

    public void setResultConverter(final String resultConverter) {
        this.resultConverter = resultConverter;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(final String repository) {
        this.repository = repository;
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
     * @return <code>true</code> if 'single' was not overwritten.
     */
    protected boolean isUsingPluginSingleConfig() {
        return !singleOverwritten;
    }

    /**
     * @return <code>true</code> if 'batch' was not overwritten.
     */
    protected boolean isUsingPluginBatchConfig() {
        return !batchOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamEager' was not overwritten.
     */
    protected boolean isUsingPluginStreamEagerConfig() {
        return !streamEagerOverwritten;
    }

    /**
     * @return <code>true</code> if 'streamLazy' was not overwritten.
     */
    protected boolean isUsingPluginStreamLazyConfig() {
        return !streamLazyOverwritten;
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

}
