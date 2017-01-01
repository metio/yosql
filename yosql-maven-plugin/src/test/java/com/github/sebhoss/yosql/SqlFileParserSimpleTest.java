package com.github.sebhoss.yosql;

import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SqlFileParserSimpleTest {

    private SqlFileParser parser;

    @Before
    public void setUp() {
        parser = new SqlFileParser(new PluginErrors());
    }

    @Test
    public void shouldReturnNotNullResultAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(new SqlSourceFile(sqlFile, null));

        // then
        Assert.assertNotNull(statement);
    }

    @Test
    public void shouldReturnNotNullConfigurationAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(new SqlSourceFile(sqlFile, null));
        final SqlStatementConfiguration configuration = statement.getConfiguration();

        // then
        Assert.assertNotNull(configuration);
    }

    @Test
    public void shouldReturnNotNullStatementAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(new SqlSourceFile(sqlFile, null));
        final String sqlStatement = statement.getStatement();

        // then
        Assert.assertNotNull(sqlStatement);
    }

    @Test
    public void shouldReturnCorrectNameAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(new SqlSourceFile(sqlFile, null));
        final SqlStatementConfiguration configuration = statement.getConfiguration();

        // then
        Assert.assertEquals("simple", configuration.getName());
    }

}
