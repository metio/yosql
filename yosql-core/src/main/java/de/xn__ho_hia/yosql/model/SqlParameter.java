/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

/**
 * Represents a single input parameter of a SQL statement.
 */
@SuppressWarnings({ "nls" })
public class SqlParameter {

    private String name;
    private String type = "java.lang.Object";
    private String converter;
    private int[]  indices;

    /**
     * @return The name of the parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name of the parameter.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return The fully-qualified type name.
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            The fully-qualified type name.
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * @return The type fully-qualified name of the converter to use.
     */
    public String getConverter() {
        return converter;
    }

    /**
     * @param converter
     *            The type fully-qualified name of the converter to use.
     */
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

    /**
     * @return <code>true</code> in case this statement has indices, <code>false</code> otherwise.
     */
    public boolean hasIndices() {
        return indices != null && indices.length > 0;
    }

}
