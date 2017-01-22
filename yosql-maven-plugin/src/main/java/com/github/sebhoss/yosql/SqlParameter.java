package com.github.sebhoss.yosql;

public class SqlParameter {

    private String name;
    private String type = "java.lang.Object";
    private String converter;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
