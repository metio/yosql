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
import wtf.metio.yosql.models.configuration.LoggingApis;

import java.util.Optional;

/**
 * Generates logging statement in generated code.
 */
public interface LoggingGenerator {

    /**
     * Decide whether this generator can support the requested {@link LoggingApis logging API}.
     *
     * @param loggingApi The logging API to support.
     * @return true if this generator supports the given API, false otherwise.
     */
    boolean supports(LoggingApis loggingApi);

    /**
     * Log which query was picked.
     *
     * @param fieldName The name of the field that contains the query that was picked.
     * @return Log statement that references the specified field to log the picked query.
     */
    CodeBlock queryPicked(String fieldName);

    /**
     * Log which parameter index was picked.
     *
     * @param fieldName The name of the field that contains the SQL statement parameter index.
     * @return Log statement that references the specified field to log the picked index.
     */
    CodeBlock indexPicked(String fieldName);

    /**
     * Log which vendor query was picked.
     *
     * @param fieldName The name of the field that contains the vendor query that was picked.
     * @return Log statement that references the specified field to log the picked vendor query.
     */
    CodeBlock vendorQueryPicked(String fieldName);

    /**
     * Log which vendor parameter index was picked.
     *
     * @param fieldName The name of the field that contains the vendor SQL statement parameter index.
     * @return Log statement that references the specified field to log the picked vendor index.
     */
    CodeBlock vendorIndexPicked(String fieldName);

    /**
     * Log the detected database vendor.
     *
     * @return Log statement that prints the database vendor.
     */
    CodeBlock vendorDetected();

    /**
     * Log the SQL statement before executing it.
     *
     * @return Log statement that prints the SQL statement.
     */
    CodeBlock executingQuery();

    /**
     * Code guard that checks whether log statements should be executed, depending on the configured log level.
     * This is used in generated code.
     *
     * @return If-then-else branch that decides whether to execute log statements.
     */
    CodeBlock shouldLog();

    /**
     * Check whether this generator is enabled. This is used during code generation.
     *
     * @return true if this generator is enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Generate a field for a class specific logger.
     *
     * @param repoClass The Java class that should contain the logger.
     * @return The field for the logger of the given class.
     */
    Optional<FieldSpec> logger(TypeName repoClass);

    /**
     * Log that a method was entered.
     *
     * @param repository The name of the enclosing class.
     * @param method The name of the enclosing method.
     * @return Log statement that prints which class/method was entered.
     */
    CodeBlock entering(String repository, String method);

}
