/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.logging;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.models.configuration.LoggingApis;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logging generator that uses java.util.logging.
 */
public final class JulLoggingGenerator implements LoggingGenerator {

    private final Fields fields;

    public JulLoggingGenerator(final Fields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(Logger.class, GeneratedNames.LOGGER)
                .initializer("$T.getLogger($T.class.getName())", Logger.class, repoClass)
                .build());
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return LoggingApis.JUL.equals(api);
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", GeneratedNames.LOGGER, String.class,
                        "Detected database vendor [%s]", GeneratedNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", GeneratedNames.LOGGER, String.class,
                        "Executing query [%s]", GeneratedNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isLoggable($T.FINE)", GeneratedNames.LOGGER, Level.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.entering($S, $S)", GeneratedNames.LOGGER, repository, method)
                .build();
    }

}
