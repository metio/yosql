/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.logging;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

import java.util.Optional;
import java.util.Set;

/**
 * Delegates logging statement generation to a set of {@link LoggingGenerator}s.
 */
public final class DelegatingLoggingGenerator implements LoggingGenerator {

    private final LoggingConfiguration loggingConfiguration;
    private final Set<LoggingGenerator> generators;

    public DelegatingLoggingGenerator(
            final LoggingConfiguration loggingConfiguration,
            final Set<LoggingGenerator> generators) {
        this.loggingConfiguration = loggingConfiguration;
        this.generators = generators;
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return generators.stream().anyMatch(generator -> generator.supports(api));
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return log().queryPicked(fieldName);
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return log().indexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return log().vendorQueryPicked(fieldName);
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return log().vendorIndexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorDetected() {
        return log().vendorDetected();
    }

    @Override
    public CodeBlock executingQuery() {
        return log().executingQuery();
    }

    @Override
    public CodeBlock shouldLog() {
        return log().shouldLog();
    }

    @Override
    public boolean isEnabled() {
        return log().isEnabled();
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return log().logger(repoClass);
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return log().entering(repository, method);
    }

    private LoggingGenerator log() {
        return generators.stream()
                .filter(generator -> generator.supports(loggingConfiguration.api()))
                .findFirst()
                .orElseThrow(() -> new CodeGenerationException("TODO: add error message for missing support for " + loggingConfiguration.api()));
    }

}
