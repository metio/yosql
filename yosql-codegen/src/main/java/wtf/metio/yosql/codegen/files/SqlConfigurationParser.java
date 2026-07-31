/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Parses strings into {@link SqlConfiguration}s.
 */
public interface SqlConfigurationParser {

    SqlConfiguration parseConfig(String rawConfig);

}
