/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.helpers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import wtf.metio.yosql.model.SqlConfiguration;
import wtf.metio.yosql.model.SqlStatement;
import wtf.metio.yosql.testutils.ValidationFile;
import wtf.metio.yosql.testutils.ValidationFileTest;

class TypicalJavadocTest extends ValidationFileTest {

    @Test
    public void shouldGenerateJavaDocForRepositories(final ValidationFile validationFile) {
        validate(TypicalJavadoc.repositoryJavadoc(createTestSqlStatement()), validationFile);
    }

    @Test
    public void shouldGenerateJavaDocForRepositoriesWithMultipleStatements(final ValidationFile validationFile) {
        validate(TypicalJavadoc.repositoryJavadoc(createTestSqlStatements(3)), validationFile);
    }

    @Test
    public void shouldGenerateJavaDocForMethods(final ValidationFile validationFile) {
        validate(TypicalJavadoc.methodJavadoc(createTestSqlStatement()), validationFile);
    }

    @Test
    public void shouldGenerateJavaDocForMethodsWithMultipleStatements(final ValidationFile validationFile) {
        validate(TypicalJavadoc.methodJavadoc(createTestSqlStatements(3)), validationFile);
    }

    private static List<SqlStatement> createTestSqlStatement() {
        final List<SqlStatement> statements = new ArrayList<>();
        final Path path = Paths.get("test/path/file.sql");
        final SqlConfiguration config = new SqlConfiguration();
        final String raw = "select x from t";
        final SqlStatement statement = new SqlStatement(path, config, raw);
        statements.add(statement);
        return statements;
    }

    private static List<SqlStatement> createTestSqlStatements(final int numberOfStatements) {
        final List<SqlStatement> statements = new ArrayList<>();
        for (int index = 0; index < numberOfStatements; index++) {
            final Path path = Paths.get("test/path/file" + index + ".sql");
            final SqlConfiguration config = new SqlConfiguration();
            config.setVendor("vendor" + index);
            final String raw = "select x from t" + index;
            final SqlStatement statement = new SqlStatement(path, config, raw);
            statements.add(statement);
        }
        return statements;
    }

}
