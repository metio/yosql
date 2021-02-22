/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.tooling.codegen.generator.api.ConstructorGenerator;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.GenericBlocks;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Methods;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcNames;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcParameters;
import wtf.metio.yosql.tooling.codegen.sql.SqlStatement;

import java.util.List;

public final class SpringJdbcConstructorGenerator implements ConstructorGenerator {

    private final GenericBlocks blocks;
    private final Methods methods;
    private final JdbcNames jdbcNames;
    private final JdbcParameters jdbcParameters;

    public SpringJdbcConstructorGenerator(
            final GenericBlocks blocks,
            final Methods methods,
            final JdbcNames jdbcNames,
            final JdbcParameters jdbcParameters) {
        this.blocks = blocks;
        this.methods = methods;
        this.jdbcNames = jdbcNames;
        this.jdbcParameters = jdbcParameters;
    }

    @Override
    public MethodSpec forRepository(final List<SqlStatement> statements) {
        return null;
    }
}
