/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.nio.file.Path;

/**
 * Validates the names of methods.
 *
 * @see DefaultSqlConfigurationFactory
 */
@FunctionalInterface
public interface MethodNameValidator {

    void validateNames(SqlConfiguration configuration, Path source);

}
