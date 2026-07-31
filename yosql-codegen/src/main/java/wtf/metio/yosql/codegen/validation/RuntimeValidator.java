/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
