package com.github.sebhoss.yosql.generator.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.github.sebhoss.yosql.generator.LoggingGenerator;
import com.github.sebhoss.yosql.generator.helpers.TypicalFields;
import com.github.sebhoss.yosql.generator.helpers.TypicalNames;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

@Named
@Singleton
public class JdkLoggingGenerator implements LoggingGenerator {

    private final TypicalFields fields;

    @Inject
    public JdkLoggingGenerator(final TypicalFields fields) {
        this.fields = fields;
    }

    public FieldSpec logger(final TypeName repoClass) {
        return fields.prepareConstant(getClass(), Logger.class, TypicalNames.LOGGER)
                .initializer("$T.getLogger($T.class.getName())", Logger.class, repoClass)
                .build();
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", TypicalNames.LOGGER, "Picked query [%s]",
                        fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", TypicalNames.LOGGER,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", TypicalNames.LOGGER, String.class,
                        "Detected database vendor [%s]", TypicalNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", TypicalNames.LOGGER, String.class,
                        "Executing query [%s]", TypicalNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public CodeBlock shouldLogLow() {
        return CodeBlock.builder().add("$N.isLoggable($T.FINE)", TypicalNames.LOGGER, Level.class).build();
    }

}
