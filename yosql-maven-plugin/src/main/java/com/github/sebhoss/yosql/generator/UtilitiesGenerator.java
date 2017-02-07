package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;

public interface UtilitiesGenerator {

    void generateUtilities(List<SqlStatement> allStatements);

}
