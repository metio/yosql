package com.github.sebhoss.yosql;

public class ConverterConfiguration {

    private String alias;
    private String type;
    private String converter;

    public String getAlias() {
        return alias;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getConverter() {
        return converter;
    }

    public void setConverter(final String converter) {
        this.converter = converter;
    }

}
