package com.github.sebhoss.yosql;

public class ParameterConverterConfiguration {

    private String alias;
    private String parameterType;
    private String converterClass;

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

    public String getConverterClass() {
        return converterClass;
    }

    public void setConverterClass(final String converterClass) {
        this.converterClass = converterClass;
    }

}
