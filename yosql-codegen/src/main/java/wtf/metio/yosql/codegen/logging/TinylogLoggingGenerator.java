/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.logging;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

import java.util.Optional;

/**
 * Logging generator that uses tinylog.
 */
public final class TinylogLoggingGenerator implements LoggingGenerator {

    private static final ClassName TINY_LOG_LOGGER = ClassName.get("org.tinylog", "Logger");

    private final NamesConfiguration names;

    public TinylogLoggingGenerator(final NamesConfiguration names) {
        this.names = names;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.empty();
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return LoggingApis.TINYLOG.equals(api);
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", TINY_LOG_LOGGER, String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", TINY_LOG_LOGGER, String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", TINY_LOG_LOGGER, String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", TINY_LOG_LOGGER, String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$T.info(() -> $T.format($S, $N))", TINY_LOG_LOGGER, String.class,
                        "Detected database vendor [%s]", names.databaseProductName())
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$T.info(() -> $T.format($S, $N))", TINY_LOG_LOGGER, String.class,
                        "Executing query [%s]", names.executedQuery())
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$T.isInfoEnabled()", TINY_LOG_LOGGER).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S, $S))", TINY_LOG_LOGGER, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
