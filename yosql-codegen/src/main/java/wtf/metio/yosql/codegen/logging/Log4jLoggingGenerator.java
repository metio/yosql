/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.codegen.logging;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.blocks.Fields;
import wtf.metio.yosql.models.configuration.LoggingApis;

import java.util.Optional;

/**
 * Logging generator that uses log4j.
 */
public final class Log4jLoggingGenerator implements LoggingGenerator {

    private static final ClassName LOG4J_LOGGER = ClassName.get("org.apache.logging.log4j", "Logger");
    private static final ClassName LOG4J_LEVEL = ClassName.get("org.apache.logging.log4j", "Level");
    private static final ClassName LOG4J_LOG_MANAGER = ClassName.get("org.apache.logging.log4j", "LogManager");

    private final Fields fields;

    public Log4jLoggingGenerator(final Fields fields) {
        this.fields = fields;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return Optional.of(fields.prepareConstant(LOG4J_LOGGER, GeneratedNames.LOGGER)
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
                .addStatement("$N.debug(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked query [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S))", GeneratedNames.LOGGER, String.class,
                        "Picked index [%s]", fieldName)
                .build();
    }

    @Override
    public CodeBlock vendorDetected() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", GeneratedNames.LOGGER, String.class,
                        "Detected database vendor [%s]", GeneratedNames.DATABASE_PRODUCT_NAME)
                .build();
    }

    @Override
    public CodeBlock executingQuery() {
        return CodeBlock.builder()
                .addStatement("$N.info(() -> $T.format($S, $N))", GeneratedNames.LOGGER, String.class,
                        "Executing query [%s]", GeneratedNames.EXECUTED_QUERY)
                .build();
    }

    @Override
    public CodeBlock shouldLog() {
        return CodeBlock.builder().add("$N.isEnabled($T.INFO)", GeneratedNames.LOGGER, LOG4J_LEVEL).build();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return CodeBlock.builder()
                .addStatement("$N.debug(() -> $T.format($S, $S, $S))", GeneratedNames.LOGGER, String.class,
                        "Entering [%s#%s]", repository, method)
                .build();
    }

}
