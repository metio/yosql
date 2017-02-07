package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;

public interface RepositoryFieldGenerator {

    CodeBlock staticInitializer(List<SqlStatement> statements);

    Iterable<FieldSpec> asFields(List<SqlStatement> statements);

}
