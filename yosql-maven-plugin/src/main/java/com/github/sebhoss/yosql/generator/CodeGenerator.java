package com.github.sebhoss.yosql.generator;

import java.util.List;

import com.github.sebhoss.yosql.model.SqlStatement;

public interface CodeGenerator {

    /**
     * Generates a single repository.
     *
     * @param repositoryName
     *            The fully-qualified name of the repository to generate.
     * @param sqlStatements
     *            The SQL statements to be included in the repository.
     */
    void generateRepository(String repositoryName, List<SqlStatement> statements);

    void generateUtilities(List<SqlStatement> statements);

}
