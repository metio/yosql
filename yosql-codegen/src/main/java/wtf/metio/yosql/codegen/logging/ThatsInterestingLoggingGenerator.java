/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.logging;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.models.configuration.LoggingApis;

import java.util.Optional;

/**
 * Logging generator that uses thats-interesting.
 */
public final class ThatsInterestingLoggingGenerator implements LoggingGenerator {

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.empty();
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return LoggingApis.NONE.equals(api);
    }

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

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder().build();
    }

}
