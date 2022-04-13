/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.sansorm;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.BatchMethodGenerator;
import wtf.metio.yosql.codegen.api.ControlFlows;
import wtf.metio.yosql.codegen.api.Methods;
import wtf.metio.yosql.codegen.api.Parameters;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class SansOrmBatchMethodGenerator implements BatchMethodGenerator {

    private final ControlFlows controlFlow;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;

    public SansOrmBatchMethodGenerator(
            final ControlFlows controlFlow,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging) {
        this.controlFlow = controlFlow;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
    }

    @Override
    public MethodSpec batchWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

}
