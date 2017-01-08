package com.github.sebhoss.yosql;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public class SqlStatementConfiguration {

    private String             name;
    private boolean            batch        = true;
    private String             batchPrefix  = "batch";
    private boolean            stream       = true;
    private String             streamPrefix = "stream";
    private List<SqlParameter> parameters;
    private String             resultConverter;
    private String             repository;

    public Iterable<ParameterSpec> getParameterSpecs() {
        return Optional.ofNullable(parameters)
                .map(params -> params.stream()
                        .map(param -> {
                            final TypeName type = ClassName.bestGuess(param.getType());
                            return ParameterSpec.builder(type, param.getName(), Modifier.FINAL).build();
                        })
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(final boolean batch) {
        this.batch = batch;
    }

    public String getBatchPrefix() {
        return batchPrefix;
    }

    public void setBatchPrefix(final String batchPrefix) {
        this.batchPrefix = batchPrefix;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(final boolean stream) {
        this.stream = stream;
    }

    public String getStreamPrefix() {
        return streamPrefix;
    }

    public void setStreamPrefix(final String streamPrefix) {
        this.streamPrefix = streamPrefix;
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

}
