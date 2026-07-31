/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
