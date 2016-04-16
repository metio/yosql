package com.github.sebhoss.yosql;

public interface SqlInputConverter<USER_TYPE, DATABASE_TYPE> {

    DATABASE_TYPE convert(USER_TYPE input);

}
