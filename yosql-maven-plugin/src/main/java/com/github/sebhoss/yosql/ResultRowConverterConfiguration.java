package com.github.sebhoss.yosql;

public class ResultRowConverterConfiguration {

    private String alias;
    private String resultType;
    private String converterClass;

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(final String resultType) {
        this.resultType = resultType;
    }

    public String getConverterClass() {
        return converterClass;
    }

    public void setConverterClass(final String converterClass) {
        this.converterClass = converterClass;
    }

}
