package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.MethodSpec;

public interface Java8StreamMethodGenerator {

    MethodSpec streamEagerMethod(List<SqlStatement> statements);

    MethodSpec streamLazyMethod(List<SqlStatement> statements);

}
