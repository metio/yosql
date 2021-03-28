/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.logging.noop;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.LoggingApis;

import java.util.Optional;

/**
 * No-op logging generator. Use when no logging statements should be generated.
 */
public final class NoOpLoggingGenerator implements LoggingGenerator {

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
