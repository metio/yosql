/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.dao.spring_jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.tooling.codegen.generator.api.LoggingGenerator;
import wtf.metio.yosql.tooling.codegen.generator.api.RxJavaMethodGenerator;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.ControlFlows;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Methods;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Names;
import wtf.metio.yosql.tooling.codegen.generator.blocks.generic.Parameters;
import wtf.metio.yosql.tooling.codegen.generator.blocks.jdbc.JdbcBlocks;
import wtf.metio.yosql.tooling.codegen.model.configuration.RuntimeConfiguration;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlConfiguration;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlStatement;

import java.util.List;

public final class SpringJdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final RuntimeConfiguration configuration;
    private final ControlFlows controlFlows;
    private final Names names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;
    private final JdbcBlocks jdbcBlocks;

    public SpringJdbcRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging,
            final JdbcBlocks jdbcBlocks) {
        this.configuration = configuration;
        this.controlFlows = controlFlows;
        this.names = names;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
        this.jdbcBlocks = jdbcBlocks;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }
}
