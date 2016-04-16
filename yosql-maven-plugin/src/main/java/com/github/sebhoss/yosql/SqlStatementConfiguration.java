package com.github.sebhoss.yosql;

import java.util.List;

public class SqlStatementConfiguration {

    private String name;
    private boolean batch = true;
    private String batchPrefix = "batch";
    private boolean stream = true;
    private String streamPrefix = "stream";
    private List<SqlParameter> parameters;
    private String resultConverter;
    private String repository;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBatch() {
        return batch;
    }

    public void setBatch(boolean batch) {
        this.batch = batch;
    }

    public String getBatchPrefix() {
        return batchPrefix;
    }

    public void setBatchPrefix(String batchPrefix) {
        this.batchPrefix = batchPrefix;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public String getStreamPrefix() {
        return streamPrefix;
    }

    public void setStreamPrefix(String streamPrefix) {
        this.streamPrefix = streamPrefix;
    }

    public List<SqlParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<SqlParameter> parameters) {
        this.parameters = parameters;
    }

    public String getResultConverter() {
        return resultConverter;
    }

    public void setResultConverter(String resultConverter) {
        this.resultConverter = resultConverter;
    }

    public String getRepository() {
        return repository;
    }

    public void setRepository(String repository) {
        this.repository = repository;
    }

}
