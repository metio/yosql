/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.logging.tinylog;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.tinylog.Logger;
import wtf.metio.yosql.codegen.api.Fields;
import wtf.metio.yosql.codegen.api.Names;
import wtf.metio.yosql.logging.api.LoggingGenerator;
import wtf.metio.yosql.models.constants.api.LoggingApis;

import java.util.Optional;

/**
 * Logging generator that uses tinylog.
 */
public final class TinylogLoggingGenerator implements LoggingGenerator {

    private final Names names;

    public TinylogLoggingGenerator(final Names names) {
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
                .addStatement("$T.debug(() -> $T.format($S, $S))", Logger.class, String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", Logger.class, String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", Logger.class, String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S))", Logger.class, String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$T.info(() -> $T.format($S, $N))", Logger.class, String.class,
                        "Detected database vendor [%s]", names.databaseProductName())
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$T.info(() -> $T.format($S, $N))", Logger.class, String.class,
                        "Executing query [%s]", names.executedQuery())
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$T.isInfoEnabled()", Logger.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$T.debug(() -> $T.format($S, $S, $S))", Logger.class, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
