/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.generic;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.model.sql.SqlConfiguration;

public interface ControlFlows {

    CodeBlock tryWithResource(CodeBlock resources);

    CodeBlock catchAndDo(CodeBlock statement);

    CodeBlock catchAndRethrow();

    CodeBlock maybeCatchAndRethrow(SqlConfiguration configuration);

    CodeBlock forLoop(CodeBlock init, CodeBlock runner);

    CodeBlock startTryBlock();
    CodeBlock endTryBlock();
    CodeBlock endTryBlock(int flowsToClose);

    CodeBlock maybeTry(SqlConfiguration configuration);
    CodeBlock endMaybeTry(SqlConfiguration configuration);

    CodeBlock ifHasNext();
    CodeBlock endIf();

    CodeBlock whileHasNext();

    CodeBlock nextElse();

}
