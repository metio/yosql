/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.spring.data.jdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.*;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class SpringDataJdbcRxJavaMethodGenerator implements RxJavaMethodGenerator {

    private final RuntimeConfiguration configuration;
    private final ControlFlows controlFlows;
    private final Names names;
    private final Methods methods;
    private final Parameters parameters;
    private final LoggingGenerator logging;

    public SpringDataJdbcRxJavaMethodGenerator(
            final RuntimeConfiguration configuration,
            final ControlFlows controlFlows,
            final Names names,
            final Methods methods,
            final Parameters parameters,
            final LoggingGenerator logging) {
        this.configuration = configuration;
        this.controlFlows = controlFlows;
        this.names = names;
        this.methods = methods;
        this.parameters = parameters;
        this.logging = logging;
    }

    @Override
    public MethodSpec rxJava2ReadMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }
}
