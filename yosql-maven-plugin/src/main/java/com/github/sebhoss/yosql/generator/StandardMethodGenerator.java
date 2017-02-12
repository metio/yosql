package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface StandardMethodGenerator {

    MethodSpec standardReadMethod(String methodName, List<SqlStatement> statements);

    MethodSpec standardWriteMethod(String methodName, List<SqlStatement> statements);

    MethodSpec standardCallMethod(String methodName, List<SqlStatement> statements);

}
