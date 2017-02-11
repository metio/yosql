package com.github.sebhoss.yosql;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.sebhoss.yosql.model.SqlSourceFile;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.parser.SqlFileParser;
import com.github.sebhoss.yosql.plugin.PluginErrors;

public class SqlFileParserSimpleTest {

    private SqlFileParser parser;

    @Before
    public void setUp() {
        parser = new SqlFileParser(new PluginErrors(), null, null);
    }

    @Test
    public void shouldReturnNotNullResultAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser.parse(new SqlSourceFile(sqlFile, null));

        // then
        Assert.assertNotNull(statement);
    }

    @Test
    public void shouldReturnNotNullConfigurationAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser.parse(new SqlSourceFile(sqlFile, null));
        final SqlConfiguration configuration = SqlConfiguration
                .merge(statement.collect(Collectors.toList()));

        // then
        Assert.assertNotNull(configuration);
    }

    @Test
    public void shouldReturnNotNullStatementAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final Stream<SqlStatement> statements = parser.parse(new SqlSourceFile(sqlFile, null));
        final String sqlStatement = statements.map(SqlStatement::getRawStatement).findFirst().orElse(null);

        // then
        Assert.assertNotNull(sqlStatement);
    }

    @Test
    public void shouldReturnCorrectNameAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser.parse(new SqlSourceFile(sqlFile, null));
        final SqlConfiguration configuration = SqlConfiguration
                .merge(statement.collect(Collectors.toList()));

        // then
        Assert.assertEquals("simple", configuration.getName());
    }

}
