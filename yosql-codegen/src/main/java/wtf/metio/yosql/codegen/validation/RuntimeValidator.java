/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.validation;

/**
 * Validates the runtime environment before generating code.
 */
@FunctionalInterface
public interface RuntimeValidator {

    /**
     * Validate the runtime environment.
     */
    void validate();

}
