/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and log4j as logging implementation using a large
 * sample size of repositories. It can be compared against the no-op implementation to check how much extra time is
 * spent by YoSQL to generate logging code using the log4j API.
 */
public class LargeJdbcLog4jBenchmark extends AbstractLargeSampleBenchmark {

    @Override
    protected LoggingConfiguration loggingConfig() {
        return LoggingConfigurations.log4j();
    }

}
