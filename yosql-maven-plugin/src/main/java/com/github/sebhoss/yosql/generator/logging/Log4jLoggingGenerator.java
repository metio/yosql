package com.github.sebhoss.yosql.generator.logging;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.sebhoss.yosql.generator.LoggingGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalFields;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

@Named
@Singleton
public class Log4jLoggingGenerator implements LoggingGenerator {

    private final TypicalFields fields;

    @Inject
    public Log4jLoggingGenerator(final TypicalFields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(getClass(), Logger.class, TypicalNames.LOGGER)
                .initializer("$T.getLogger($T.class)", LogManager.class, repoClass)
                .build());
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> String.format($S, $S))", TypicalNames.LOGGER, "Picked query [%s]",
                        fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", TypicalNames.LOGGER, String.class,
                        "Detected database vendor [%s]", TypicalNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", TypicalNames.LOGGER, String.class,
                        "Executing query [%s]", TypicalNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isEnabled($T.INFO)", TypicalNames.LOGGER, Level.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $S, $S))", TypicalNames.LOGGER, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
