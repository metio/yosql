/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import wtf.metio.yosql.models.immutables.SqlConfiguration;

/**
 * Parses strings into {@link SqlConfiguration}s.
 */
public interface SqlConfigurationParser {

    SqlConfiguration parseConfig(String rawConfig);

}
