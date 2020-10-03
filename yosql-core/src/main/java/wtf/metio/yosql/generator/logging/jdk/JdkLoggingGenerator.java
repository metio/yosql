/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging.jdk;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.generator.blocks.api.Fields;
import wtf.metio.yosql.generator.blocks.api.Names;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

final class JdkLoggingGenerator implements LoggingGenerator {

    private final Names names;
    private final Fields fields;

    JdkLoggingGenerator(final Names names, final Fields fields) {
        this.names = names;
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(Logger.class, names.logger())
                .initializer("$T.getLogger($T.class.getName())", Logger.class, repoClass)
                .build());
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", names.logger(), "Picked query [%s]",
                        fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", names.logger(),
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", names.logger(),
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.finer(() -> String.format($S, $S))", names.logger(),
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", names.logger(), String.class,
                        "Detected database vendor [%s]", names.databaseProductName())
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.fine(() -> $T.format($S, $N))", names.logger(), String.class,
                        "Executing query [%s]", names.executedQuery())
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isLoggable($T.FINE)", names.logger(), Level.class).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.entering($S, $S)", names.logger(), repository, method)
                .build();
    }

}
