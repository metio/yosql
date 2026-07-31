/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.codegen;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and no logging output. It can be used as a baseline to measure
 * against in order to check code generation performance while using a medium sample size.
 */
public class MediumJdbcNoOpBenchmark extends AbstractMediumSampleBenchmark {

    // default config already uses no-op logging

}
