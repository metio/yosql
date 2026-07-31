/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
