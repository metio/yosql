/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.tooling.codegen;

import wtf.metio.yosql.tooling.codegen.files.FileParser;
import wtf.metio.yosql.tooling.codegen.generator.api.CodeGenerator;
import wtf.metio.yosql.tooling.codegen.orchestration.Orchestrator;

/**
 * Default implementation of YoSQL. Its responsible for the high-level orchestration of a single code generation run
 * (which can be called multiple times if desired). Delegates most of the actual work to various interfaces whose
 * implementation must be provided at runtime.
 */
final class OrchestratedYoSQL implements YoSQL {

    private final Orchestrator orchestrator;
    private final FileParser fileParser;
    private final CodeGenerator codeGenerator;

    OrchestratedYoSQL(
            final Orchestrator orchestrator,
            final FileParser fileParser,
            final CodeGenerator codeGenerator) {
        this.orchestrator = orchestrator;
        this.fileParser = fileParser;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void generateCode() {
        orchestrator.execute(fileParser::parseFiles, codeGenerator::generateCode);
    }

}
