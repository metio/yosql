/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.CodeBlock;

public interface JdbcMethods {

    JdbcDataSourceMethods dataSource();
    JdbcConnectionMethods connection();
    JdbcResultSetMethods resultSet();
    JdbcMetaDataMethods metaData();
    JdbcStatementMethods statement();

    interface JdbcDataSourceMethods {
        CodeBlock getConnection();
    }
    interface JdbcConnectionMethods {
        CodeBlock prepareStatement();
        CodeBlock prepareCallable();
    }
    interface JdbcResultSetMethods {
        CodeBlock getMetaData();
    }
    interface JdbcMetaDataMethods {
        CodeBlock getColumnCount();
    }
    interface JdbcStatementMethods {
        CodeBlock executeQuery();
        CodeBlock executeUpdate();
        CodeBlock executeBatch();
        CodeBlock addBatch();
    }

}
