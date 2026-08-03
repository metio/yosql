/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.SQLException;
import java.util.stream.IntStream;

import static wtf.metio.yosql.codegen.blocks.CodeBlocks.code;

public final class DefaultControlFlows implements ControlFlows {

    private final Variables variables;

    public DefaultControlFlows(
            final Variables variables) {
        this.variables = variables;
    }

    @Override
    public CodeBlock tryWithResource(final CodeBlock resources) {
        return CodeBlock.builder()
                .beginControlFlow("try ($L)", resources)
                .build();
    }

    @Override
    public CodeBlock catchAndDo(final CodeBlock statement) {
        final var catchDeclaration = variables.inline(SQLException.class, GeneratedNames.EXCEPTION);
        return CodeBlock.builder()
                .beginControlFlow("catch ($L)", catchDeclaration)
                .addStatement(statement)
                .endControlFlow()
                .build();
    }

    @Override
    public CodeBlock catchAndRethrow() {
        return catchAndDo(code("throw new $T($N)", RuntimeException.class, GeneratedNames.EXCEPTION));
    }

    @Override
    public CodeBlock maybeCatchAndRethrow(final SqlConfiguration configuration) {
        return configuration.catchAndRethrow()
                .filter(Boolean.TRUE::equals)
                .map(bool -> catchAndRethrow())
                .orElse(CodeBlock.builder().build());
    }

    @Override
    public CodeBlock forLoop(final CodeBlock init, final CodeBlock runner) {
        return CodeBlock.builder()
                .beginControlFlow("for ($L)", init)
                .add(runner)
                .endControlFlow()
                .build();
    }

    @Override
    public CodeBlock startTryBlock() {
        return CodeBlock.builder().beginControlFlow("try").build();
    }

    @Override
    public CodeBlock endTryBlock() {
        return endTryBlock(1);
    }

    @Override
    public CodeBlock endTryBlock(final int flowsToClose) {
        final var builder = CodeBlock.builder();
        IntStream.range(0, flowsToClose).forEach(index -> builder.endControlFlow());
        return builder.build();
    }

    @Override
    public CodeBlock maybeTry(final SqlConfiguration configuration) {
        return configuration.catchAndRethrow()
                .filter(Boolean.TRUE::equals)
                .map(bool -> startTryBlock())
                .orElse(CodeBlock.builder().build());
    }

    @Override
    public CodeBlock endMaybeTry(final SqlConfiguration configuration) {
        return configuration.catchAndRethrow()
                .filter(Boolean.TRUE::equals)
                .map(bool -> endTryBlock())
                .orElse(CodeBlock.builder().build());
    }

    @Override
    public CodeBlock ifHasNext() {
        return CodeBlock.builder()
                .beginControlFlow("if ($N.next())", GeneratedNames.RESULT_SET)
                .build();
    }

    @Override
    public CodeBlock endIf() {
        return CodeBlock.builder().endControlFlow().build();
    }

    @Override
    public CodeBlock whileHasNext() {
        return CodeBlock.builder()
                .beginControlFlow("while ($N.next())", GeneratedNames.RESULT_SET)
                .build();
    }

    @Override
    public CodeBlock nextElse() {
        return CodeBlock.builder()
                .nextControlFlow("else")
                .build();
    }

}
