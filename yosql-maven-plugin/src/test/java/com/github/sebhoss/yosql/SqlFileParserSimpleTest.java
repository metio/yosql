/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package com.github.sebhoss.yosql;

import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.sebhoss.yosql.model.SqlConfiguration;
import com.github.sebhoss.yosql.model.SqlSourceFile;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.parser.SqlConfigurationFactory;
import com.github.sebhoss.yosql.parser.SqlFileParser;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.github.sebhoss.yosql.plugin.PluginErrors;

public class SqlFileParserSimpleTest {

    private SqlFileParser parser;

    @Before
    public void setUp() {
        final PluginErrors errors = new PluginErrors();
        final PluginConfig config = new PluginConfig();
        config.setSqlFilesCharset("UTF-8");
        config.setSqlStatementSeparator(";");
        config.setBasePackageName("com.example");
        config.setUtilityPackageName("util");
        config.setRepositoryNameSuffix("Repository");
        final SqlConfigurationFactory factory = new SqlConfigurationFactory(errors, config);
        parser = new SqlFileParser(errors, config, factory);
    }

    @Test
    public void shouldReturnNotNullResultAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser
                .parse(new SqlSourceFile(sqlFile, TestSqlFiles.getFullPath("/sql")));

        // then
        Assert.assertNotNull(statement);
    }

    @Test
    public void shouldReturnNotNullConfigurationAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser
                .parse(new SqlSourceFile(sqlFile, TestSqlFiles.getFullPath("/sql")));
        final SqlConfiguration configuration = SqlConfiguration
                .merge(statement.collect(Collectors.toList()));

        // then
        Assert.assertNotNull(configuration);
    }

    @Test
    public void shouldReturnNotNullStatementAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple/simple.sql");

        // when
        final Stream<SqlStatement> statements = parser
                .parse(new SqlSourceFile(sqlFile, TestSqlFiles.getFullPath("/sql")));
        final String sqlStatement = statements.map(SqlStatement::getRawStatement).findFirst().orElse(null);

        // then
        Assert.assertNotNull(sqlStatement);
    }

    @Test
    public void shouldReturnCorrectNameAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple/simple.sql");

        // when
        final Stream<SqlStatement> statement = parser
                .parse(new SqlSourceFile(sqlFile, TestSqlFiles.getFullPath("/sql")));
        final SqlConfiguration configuration = SqlConfiguration
                .merge(statement.collect(Collectors.toList()));

        // then
        Assert.assertEquals("simple", configuration.getName());
    }

}
