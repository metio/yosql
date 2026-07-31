/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.codegen.orchestration;

/**
 * High-level interface of YoSQL. All configuration options must be passed into the actual implementation
 * or otherwise obtained before generating code.
 */
public interface YoSQL {

    /**
     * Generates .java files based on the configured .sql files and generator options.
     */
    void generateCode();

}
