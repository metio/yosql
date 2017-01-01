package com.github.sebhoss.yosql;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Named
@Singleton
public class CodeGenerator {

    /**
     * Generates a single repository.
     *
     * @param repositoryName The fully qualified name of the repository.
     * @param sqlStatements The SQL statements to be included in the repository.
     */
    public void generateRepository(final String repositoryName, final List<SqlStatement> sqlStatements) {
        // TODO: Write repository + add all SQL statements as methods to it
    }

}
