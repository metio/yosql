/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.logging.api;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;

import java.util.List;
import java.util.Optional;

public final class DelegatingLoggingGenerator implements LoggingGenerator {

    private final ApiConfiguration apiConfiguration;
    private final List<LoggingGenerator> generators;

    public DelegatingLoggingGenerator(
            final ApiConfiguration apiConfiguration,
            final LoggingGenerator... generators) {
        this.apiConfiguration = apiConfiguration;
        this.generators = List.of(generators);
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
                .filter(generator -> generator.supports(apiConfiguration.loggingApi()))
                .findFirst()
                .orElseThrow(() -> new CodeGenerationException("TODO: add error message for missing support for " + apiConfiguration.loggingApi()));
    }

}
