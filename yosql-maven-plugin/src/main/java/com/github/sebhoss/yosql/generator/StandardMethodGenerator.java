package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface StandardMethodGenerator {

    MethodSpec standardReadMethod(String methodName, SqlConfiguration mergedConfiguration,
            List<SqlStatement> statements);

    MethodSpec standardWriteMethod(String methodName, SqlConfiguration mergedConfiguration,
            List<SqlStatement> statements);

    MethodSpec standardCallMethod(String methodName, SqlConfiguration mergedConfiguration,
            List<SqlStatement> statements);

}
