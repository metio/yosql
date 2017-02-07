package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface Java8StreamMethodGenerator {

    MethodSpec streamEagerApi(List<SqlStatement> statements);

    MethodSpec streamLazyApi(List<SqlStatement> statements);

}
