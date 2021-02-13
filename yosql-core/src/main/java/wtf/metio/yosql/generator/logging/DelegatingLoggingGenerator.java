/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.generator.logging;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.generator.api.LoggingGenerator;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

import java.util.Optional;

final class DelegatingLoggingGenerator implements LoggingGenerator {

    private final RuntimeConfiguration runtimeConfiguration;
    private final LoggingGenerator jdkLoggingGenerator;
    private final LoggingGenerator log4jLoggingGenerator;
    private final LoggingGenerator noOpLoggingGenerator;
    private final LoggingGenerator slf4jLoggingGenerator;

    DelegatingLoggingGenerator(
            final RuntimeConfiguration runtimeConfiguration,
            final LoggingGenerator jdkLoggingGenerator,
            final LoggingGenerator log4jLoggingGenerator,
            final LoggingGenerator noOpLoggingGenerator,
            final LoggingGenerator slf4jLoggingGenerator) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.jdkLoggingGenerator = jdkLoggingGenerator;
        this.log4jLoggingGenerator = log4jLoggingGenerator;
        this.noOpLoggingGenerator = noOpLoggingGenerator;
        this.slf4jLoggingGenerator = slf4jLoggingGenerator;
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return log().queryPicked(fieldName);
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return log().indexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return log().vendorQueryPicked(fieldName);
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return log().vendorIndexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorDetected() {
        return log().vendorDetected();
    }

    @Override
    public CodeBlock executingQuery() {
        return log().executingQuery();
    }

    @Override
    public CodeBlock shouldLog() {
        return log().shouldLog();
    }

    @Override
    public boolean isEnabled() {
        return log().isEnabled();
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return log().logger(repoClass);
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return log().entering(repository, method);
    }

    private LoggingGenerator log() {
        return switch (runtimeConfiguration.api().loggingApi()) {
            case JDK -> jdkLoggingGenerator;
            case LOG4J -> log4jLoggingGenerator;
            case SLF4J -> slf4jLoggingGenerator;
            default -> noOpLoggingGenerator;
        };
    }

}
