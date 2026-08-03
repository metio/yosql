/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.logging;

import ch.qos.cal10n.IMessageConveyor;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.CodeGenerationException;
import wtf.metio.yosql.codegen.lifecycle.ApplicationErrors;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

import java.util.Optional;
import java.util.Map;

/**
 * Delegates logging statement generation to a set of {@link LoggingGenerator}s.
 */
public final class DelegatingLoggingGenerator implements LoggingGenerator {

    private final LoggingConfiguration loggingConfiguration;
    private final Map<LoggingApis, LoggingGenerator> generators;
    private final IMessageConveyor messages;

    public DelegatingLoggingGenerator(
            final LoggingConfiguration loggingConfiguration,
            final Map<LoggingApis, LoggingGenerator> generators,
            final IMessageConveyor messages) {
        this.loggingConfiguration = loggingConfiguration;
        this.generators = generators;
        this.messages = messages;
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return generators.containsKey(api);
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

    /**
     * A lookup rather than a scan. Which generator serves an API is now a fact of the binding rather
     * than of iteration order, and two generators claiming the same API is a duplicate map key that
     * Dagger refuses to compile instead of one silently shadowing the other.
     */
    private LoggingGenerator log() {
        final var generator = generators.get(loggingConfiguration.api());
        if (generator == null) {
            throw new CodeGenerationException(
                    messages.getMessage(ApplicationErrors.UNSUPPORTED_LOGGING_API, loggingConfiguration.api()));
        }
        return generator;
    }

}
