package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;

public interface CodeGenerator {

    void generateRepository(String repositoryName, List<SqlStatement> statements);

    void generateUtilities(List<SqlStatement> statements);

}
