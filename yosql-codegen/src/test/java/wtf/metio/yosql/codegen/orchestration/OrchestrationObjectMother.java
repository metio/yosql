/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.orchestration;

import wtf.metio.yosql.codegen.logging.LoggingObjectMother;

public final class OrchestrationObjectMother {

    public static ExecutionErrors executionErrors() {
        return new ExecutionErrors(LoggingObjectMother.logger());
    }

    private OrchestrationObjectMother() {
        // factory class
    }

}
