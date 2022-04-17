/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.fluentjdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Java8StreamMethodGenerator;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.codegen.blocks.GenericBlocks;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.NamesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class FluentJdbcJava8StreamMethodGenerator implements Java8StreamMethodGenerator {

    private final GenericBlocks blocks;
    private final ControlFlows controlFlow;
    private final NamesConfiguration names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;

    public FluentJdbcJava8StreamMethodGenerator(
            final GenericBlocks blocks,
            final ControlFlows controlFlow,
            final NamesConfiguration names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging) {
        this.blocks = blocks;
        this.controlFlow = controlFlow;
        this.names = names;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
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
