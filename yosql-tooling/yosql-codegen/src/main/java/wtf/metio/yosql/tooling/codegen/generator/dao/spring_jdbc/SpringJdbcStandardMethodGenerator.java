/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.tooling.codegen.generator.api.LoggingGenerator;
import wtf.metio.yosql.tooling.codegen.generator.api.StandardMethodGenerator;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.ControlFlows;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Methods;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Parameters;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcTransformer;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlConfiguration;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlStatement;

import java.util.List;

public final class SpringJdbcStandardMethodGenerator implements StandardMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbc;
    private final JdbcTransformer jdbcTransformer;

    public SpringJdbcStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbc,
            final JdbcTransformer jdbcTransformer) {
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
        this.jdbc = jdbc;
        this.jdbcTransformer = jdbcTransformer;
    }

    @Override
    public MethodSpec standardReadMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec standardWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec standardCallMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }
}
