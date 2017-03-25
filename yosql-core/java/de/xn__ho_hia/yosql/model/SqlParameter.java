/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.model;

@SuppressWarnings({ "javadoc", "nls" })
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
