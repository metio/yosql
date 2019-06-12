/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging.log4j;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.helpers.TypicalFields;
import wtf.metio.yosql.generator.helpers.TypicalNames;

import java.util.Optional;

final class Log4jLoggingGenerator implements LoggingGenerator {

    private final TypicalFields fields;

    Log4jLoggingGenerator(final TypicalFields fields) {
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
                .addStatement("$N.debug(() -> $T.format($S, $S, $S))", TypicalNames.LOGGER, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
