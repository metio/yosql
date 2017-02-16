package com.github.sebhoss.yosql.generator.logging;

import com.github.sebhoss.yosql.generator.LoggingGenerator;
import com.squareup.javapoet.CodeBlock;

public class NoOpLoggingGenerator implements LoggingGenerator {

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder().build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().build();
    }

}
