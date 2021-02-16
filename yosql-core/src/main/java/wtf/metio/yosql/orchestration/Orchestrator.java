/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.orchestration;

import wtf.metio.yosql.model.internal.PackagedTypeSpec;
import wtf.metio.yosql.model.sql.SqlStatement;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Orchestrator {

    void execute(
            Supplier<List<SqlStatement>> parseFiles,
            Function<List<SqlStatement>, Stream<PackagedTypeSpec>> generateCode);
}
