package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface MethodGenerator {

    MethodSpec generateMethod(String methodName, List<SqlStatement> statements);

    String getName();

}
