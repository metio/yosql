/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.logging;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.models.configuration.LoggingApis;
import wtf.metio.yosql.models.immutables.NamesConfiguration;

import java.util.Optional;

/**
 * Logging generator that uses log4j.
 */
public final class Log4jLoggingGenerator implements LoggingGenerator {

    private static final ClassName LOG4J_LOGGER = ClassName.get("org.apache.logging.log4j", "Logger");
    private static final ClassName LOG4J_LEVEL = ClassName.get("org.apache.logging.log4j", "Level");
    private static final ClassName LOG4J_LOG_MANAGER = ClassName.get("org.apache.logging.log4j", "LogManager");

    private final NamesConfiguration names;
    private final Fields fields;

    public Log4jLoggingGenerator(final NamesConfiguration names, final Fields fields) {
        this.names = names;
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(LOG4J_LOGGER, names.logger())
                .initializer("$T.getLogger($T.class)", LOG4J_LOG_MANAGER, repoClass)
                .build());
    }

    @Override
    public boolean supports(final LoggingApis api) {
        return LoggingApis.LOG4J.equals(api);
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", names.logger(), String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", names.logger(), String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", names.logger(), String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", names.logger(), String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", names.logger(), String.class,
                        "Detected database vendor [%s]", names.databaseProductName())
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", names.logger(), String.class,
                        "Executing query [%s]", names.executedQuery())
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isEnabled($T.INFO)", names.logger(), LOG4J_LEVEL).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S, $S))", names.logger(), String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
