/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.fluentjdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.codegen.api.StandardMethodGenerator;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class FluentJdbcStandardMethodGenerator implements StandardMethodGenerator {

    private final ControlFlows controlFlows;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;

    public FluentJdbcStandardMethodGenerator(
            final ControlFlows controlFlows,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging) {
        this.controlFlows = controlFlows;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
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
