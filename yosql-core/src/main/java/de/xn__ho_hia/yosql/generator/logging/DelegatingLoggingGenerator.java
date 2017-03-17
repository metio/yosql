/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package de.xn__ho_hia.yosql.generator.logging;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import de.xn__ho_hia.yosql.generator.LoggingGenerator;
import de.xn__ho_hia.yosql.model.ExecutionConfiguration;

@Named
@Singleton
@SuppressWarnings({ "javadoc" })
public class DelegatingLoggingGenerator implements LoggingGenerator {

    private final ExecutionConfiguration          config;
    private final JdkLoggingGenerator   jdkLogging;
    private final Log4jLoggingGenerator log4jLogging;
    private final Slf4jLoggingGenerator slf4jLogging;
    private final NoOpLoggingGenerator  noOpLogging;

    @Inject
    public DelegatingLoggingGenerator(
            final ExecutionConfiguration config,
            final JdkLoggingGenerator jdkLogging,
            final Log4jLoggingGenerator log4jLogging,
            final Slf4jLoggingGenerator slf4jLogging,
            final NoOpLoggingGenerator noOpLogging) {
        this.config = config;
        this.jdkLogging = jdkLogging;
        this.log4jLogging = log4jLogging;
        this.slf4jLogging = slf4jLogging;
        this.noOpLogging = noOpLogging;
    }

    @Override
    public Optional<FieldSpec> logger(final TypeName repoClass) {
        return logging().logger(repoClass);
    }

    @Override
    public CodeBlock queryPicked(final String fieldName) {
        return logging().queryPicked(fieldName);
    }

    @Override
    public CodeBlock indexPicked(final String fieldName) {
        return logging().indexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorQueryPicked(final String fieldName) {
        return logging().vendorQueryPicked(fieldName);
    }

    @Override
    public CodeBlock vendorIndexPicked(final String fieldName) {
        return logging().vendorIndexPicked(fieldName);
    }

    @Override
    public CodeBlock vendorDetected() {
        return logging().vendorDetected();
    }

    @Override
    public CodeBlock executingQuery() {
        return logging().executingQuery();
    }

    @Override
    public CodeBlock shouldLog() {
        return logging().shouldLog();
    }

    @Override
    public boolean isEnabled() {
        return logging().isEnabled();
    }

    @Override
    public CodeBlock entering(final String repository, final String method) {
        return logging().entering(repository, method);
    }

    private LoggingGenerator logging() {
        LoggingGenerator generator = null;
        switch (config.getLoggingApi()) {
            case JDK:
                generator = jdkLogging;
                break;
            case AUTO:
                generator = jdkLogging;
                break;
            case LOG4J:
                generator = log4jLogging;
                break;
            case SLF4J:
                generator = slf4jLogging;
                break;
            default:
                generator = noOpLogging;
                break;
        }
        return generator;
    }

}
