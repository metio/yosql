/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

public interface ControlFlows {

    CodeBlock tryWithResource(CodeBlock resources);

    CodeBlock catchAndDo(CodeBlock statement);

    CodeBlock catchAndRethrow();

    /**
     * Opens a {@code finally} on the try block already begun.
     */
    CodeBlock finallyBlock();

    /**
     * Closes a resource when the block that opened it does not run to the end.
     *
     * <p>A method that hands a resource to its caller — a {@code Stream} that closes the connection
     * behind it — cannot use try-with-resources, because the whole point is that the resource
     * outlives the method. What it needs instead is the other half: everything between opening the
     * resource and handing it over is wrapped, so the one path that returns without closing is the
     * path that gave ownership away.</p>
     *
     * <p>Pair with {@link #startTryBlock()}, and close the innermost resource first. A failure while
     * closing is suppressed onto the exception that caused it rather than replacing it.</p>
     *
     * @param resource the local holding what to close
     */
    CodeBlock closeOnFailure(String resource);

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
