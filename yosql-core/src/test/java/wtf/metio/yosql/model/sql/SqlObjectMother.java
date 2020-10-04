/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.sql;

import java.nio.file.Paths;
import java.util.List;

public final class SqlObjectMother {

    public static SqlConfiguration sqlConfiguration() {
        final var config = new SqlConfiguration();
        config.setName("queryTest");
        config.getParameters().add(SqlParameter.builder()
                .setName("test")
                .setType(Object.class.getName())
                .setIndices(0)
                .setConverter("defaultRowConverter")
                .build());
        config.setResultRowConverter(ResultRowConverter.builder()
                .setAlias("defaultRowConverter")
                .setConverterType("com.example.DefaultConverter")
                .setResultType(Object.class.getName())
                .build());
        config.setRepository("Test");
        return config;
    }

    public static List<SqlStatement> sqlStatements() {
        return List.of(new SqlStatement(Paths.get("/some/path/query.sql"), sqlConfiguration(), "SELECT raw FROM table;"));
    }

    private SqlObjectMother() {
        // factory class
    }

}
