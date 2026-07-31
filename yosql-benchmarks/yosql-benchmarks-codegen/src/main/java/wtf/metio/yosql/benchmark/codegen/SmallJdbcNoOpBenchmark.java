/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.benchmark.codegen;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and no logging output. It can be used as a baseline to measure
 * against in order to check code generation performance while using a small sample size.
 */
public class SmallJdbcNoOpBenchmark extends AbstractSmallSampleBenchmark {

    // default config already uses no-op logging

}
