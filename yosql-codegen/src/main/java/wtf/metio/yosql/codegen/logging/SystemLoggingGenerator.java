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

/**
 * Logging generator that uses {@link System.Logger}.
 */
public final class SystemLoggingGenerator implements LoggingGenerator {

    private final Fields fields;

    public SystemLoggingGenerator(final Fields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(System.Logger.class, GeneratedNames.LOGGER)
                .initializer("$T.getLogger($S)", System.class, repoClass.toString())
                .build());
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return LoggingApis.SYSTEM.equals(api);
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.log($T.INFO, $T.format($S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Detected database vendor [%s]", GeneratedNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.log($T.INFO, $T.format($S, $N))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Executing query [%s]", GeneratedNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isLoggable($T.INFO)", GeneratedNames.LOGGER, System.Logger.Level.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S, $S))", GeneratedNames.LOGGER, System.Logger.Level.class,
                        String.class, "Entering [%s#%s]", repository, method)
                .build();
    }

}
