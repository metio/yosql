package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface BatchMethodGenerator {

    MethodSpec batchApi(List<SqlStatement> sqlStatements);

}
