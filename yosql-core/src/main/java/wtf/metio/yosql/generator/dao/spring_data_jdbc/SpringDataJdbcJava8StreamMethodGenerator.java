/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.dao.spring_data_jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.generator.api.Java8StreamMethodGenerator;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.generic.*;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.model.sql.SqlConfiguration;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;

public final class SpringDataJdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final GenericBlocks blocks;
    private final ControlFlows controlFlow;
    private final Names names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbcBlocks;
    private final JdbcTransformer jdbcTransformer;

    public SpringDataJdbcJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks,
            final JdbcTransformer jdbcTransformer) {
        this.blocks = blocks;
        this.controlFlow = controlFlow;
        this.names = names;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
        this.jdbcBlocks = jdbcBlocks;
        this.jdbcTransformer = jdbcTransformer;
    }

    @Override
    public MethodSpec streamEagerMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec streamLazyMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }
}
