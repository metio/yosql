package com.github.sebhoss.yosql;

public class ResultRowConverter {

    public String name;
    public String converterType;
    public String resultType;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (converterType == null ? 0 : converterType.hashCode());
        result = prime * result + (name == null ? 0 : name.hashCode());
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
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
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
