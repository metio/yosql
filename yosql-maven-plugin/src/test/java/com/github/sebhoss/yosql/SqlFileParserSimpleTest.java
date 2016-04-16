/*
 * ysura GmbH ("COMPANY") CONFIDENTIAL
 * Unpublished Copyright (c) 2012-2015 ysura GmbH, All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of COMPANY. The intellectual and technical concepts contained
 * herein are proprietary to COMPANY and may be covered by U.S. and Foreign Patents, patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden unless prior written permission is obtained
 * from COMPANY.  Access to the source code contained herein is hereby forbidden to anyone except current COMPANY employees, managers or contractors who have executed
 * Confidentiality and Non-disclosure agreements explicitly covering such access.
 *
 * The copyright notice above does not evidence any actual or intended publication or disclosure  of  this source code, which includes
 * information that is confidential and/or proprietary, and is a trade secret, of COMPANY. ANY REPRODUCTION, MODIFICATION, DISTRIBUTION, PUBLIC PERFORMANCE,
 * OR PUBLIC DISPLAY OF OR THROUGH USE  OF THIS SOURCE CODE WITHOUT THE EXPRESS WRITTEN CONSENT OF COMPANY IS STRICTLY PROHIBITED, AND IN VIOLATION OF APPLICABLE
 * LAWS AND INTERNATIONAL TREATIES. THE RECEIPT OR POSSESSION OF THIS SOURCE CODE AND/OR RELATED INFORMATION DOES NOT CONVEY OR IMPLY ANY RIGHTS
 * TO REPRODUCE, DISCLOSE OR DISTRIBUTE ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT IT MAY DESCRIBE, IN WHOLE OR IN PART.
 */
package com.github.sebhoss.yosql;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;

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
        final SqlStatement statement = parser.parse(sqlFile);

        // then
        Assert.assertNotNull(statement);
    }

    @Test
    public void shouldReturnNotNullConfigurationAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(sqlFile);
        final SqlStatementConfiguration configuration = statement.getConfiguration();

        // then
        Assert.assertNotNull(configuration);
    }

    @Test
    public void shouldReturnNotNullStatementAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(sqlFile);
        final String sqlStatement = statement.getStatement();

        // then
        Assert.assertNotNull(sqlStatement);
    }

    @Test
    public void shouldReturnCorrectNameAfterParsingSimpleSql() {
        // given
        final Path sqlFile = TestSqlFiles.getFullPath("/sql/simple.sql");

        // when
        final SqlStatement statement = parser.parse(sqlFile);
        final SqlStatementConfiguration configuration = statement.getConfiguration();

        // then
        Assert.assertEquals("simple", configuration.getName());
    }

}
