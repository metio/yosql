package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface RxJavaMethodGenerator {

    MethodSpec rxJava2ReadMethod(SqlConfiguration mergedConfiguration, List<SqlStatement> statements);

}
