package com.github.sebhoss.yosql.model;

public class ParameterConverter {

    private String alias;
    private String parameterType;
    private String converterType;

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(final String parameterType) {
        this.parameterType = parameterType;
    }

    public String getConverterType() {
        return converterType;
    }

    public void setConverterType(final String converterType) {
        this.converterType = converterType;
    }

}
