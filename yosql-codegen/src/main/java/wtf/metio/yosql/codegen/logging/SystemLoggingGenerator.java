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
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

import java.util.Optional;

/**
 * Logging generator that uses {@link System.Logger}.
 */
public final class SystemLoggingGenerator implements LoggingGenerator {

    private final NamesConfiguration names;
    private final Fields fields;

    public SystemLoggingGenerator(final NamesConfiguration names, final Fields fields) {
        this.names = names;
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(System.Logger.class, names.logger())
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
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.log($T.INFO, $T.format($S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Detected database vendor [%s]", names.databaseProductName())
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.log($T.INFO, $T.format($S, $N))", names.logger(), System.Logger.Level.class,
                        String.class, "Executing query [%s]", names.executedQuery())
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isLoggable($T.INFO)", names.logger(), System.Logger.Level.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.log($T.DEBUG, $T.format($S, $S, $S))", names.logger(), System.Logger.Level.class,
                        String.class, "Entering [%s#%s]", repository, method)
                .build();
    }

}
