package com.github.sebhoss.yosql.model;

public class SqlParameter {

    private String name;
    private String type = "java.lang.Object";
    private String converter;
    private int[]  indices;

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

    /**
     * @return the indices
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * @param indices
     *            the indices to set
     */
    public void setIndices(final int[] indices) {
        this.indices = indices;
    }

    public boolean hasIndices() {
        return indices != null && indices.length > 0;
    }

}
