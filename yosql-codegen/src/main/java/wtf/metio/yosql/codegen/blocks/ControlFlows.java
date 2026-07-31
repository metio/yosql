/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

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
