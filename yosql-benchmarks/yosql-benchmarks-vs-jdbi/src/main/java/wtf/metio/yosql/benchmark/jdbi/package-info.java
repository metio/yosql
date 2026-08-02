/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

/**
 * The same eleven scenarios written twice, once with `YoSQL` and once with JDBI, so that one JMH run
 * measures both against the same schema on the same machine.
 */
package wtf.metio.yosql.benchmark.jdbi;
