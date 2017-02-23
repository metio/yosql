/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql.model;

@SuppressWarnings({ "javadoc" })
public class ResultRowConverter {

    private String alias;
    private String converterType;
    private String resultType;

    /**
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     *            the alias to set
     */
    public void setAlias(final String alias) {
        this.alias = alias;
    }

    /**
     * @return the converterType
     */
    public String getConverterType() {
        return converterType;
    }

    /**
     * @param converterType
     *            the converterType to set
     */
    public void setConverterType(final String converterType) {
        this.converterType = converterType;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType
     *            the resultType to set
     */
    public void setResultType(final String resultType) {
        this.resultType = resultType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (converterType == null ? 0 : converterType.hashCode());
        result = prime * result + (alias == null ? 0 : alias.hashCode());
        result = prime * result + (resultType == null ? 0 : resultType.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ResultRowConverter)) {
            return false;
        }
        final ResultRowConverter other = (ResultRowConverter) obj;
        if (converterType == null) {
            if (other.converterType != null) {
                return false;
            }
        } else if (!converterType.equals(other.converterType)) {
            return false;
        }
        if (alias == null) {
            if (other.alias != null) {
                return false;
            }
        } else if (!alias.equals(other.alias)) {
            return false;
        }
        if (resultType == null) {
            if (other.resultType != null) {
                return false;
            }
        } else if (!resultType.equals(other.resultType)) {
            return false;
        }
        return true;
    }

}
