package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface BatchMethodGenerator {

    MethodSpec batchMethod(SqlConfiguration mergedConfiguration, List<SqlStatement> sqlStatements);

}
