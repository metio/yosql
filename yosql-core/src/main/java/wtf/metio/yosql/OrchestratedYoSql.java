/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql;

import wtf.metio.yosql.files.FileParser;
import wtf.metio.yosql.generator.api.CodeGenerator;
import wtf.metio.yosql.orchestration.Orchestrator;

/**
 * Default implementation of YoSql. Its responsible for the high-level orchestration
 * of a single code generation run (which can be called multiple times if desired).
 * Delegates most of the actual work to various interfaces whose implementation must
 * be injected at options.
 */
final class OrchestratedYoSql implements YoSql {

    private final Orchestrator orchestrator;
    private final FileParser files;
    private final CodeGenerator codeGenerator;

    OrchestratedYoSql(
            final Orchestrator orchestrator,
            final FileParser files,
            final CodeGenerator codeGenerator) {
        this.orchestrator = orchestrator;
        this.files = files;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void generateCode() {
        orchestrator.execute(files::parseFiles, codeGenerator::generateCode);
    }

}
