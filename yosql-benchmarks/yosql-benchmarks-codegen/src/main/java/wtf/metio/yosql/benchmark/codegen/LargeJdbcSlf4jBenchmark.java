/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and slf4j as logging implementation using a large
 * sample size of repositories. It can be compared against the no-op implementation to check how much extra time is
 * spent by YoSQL to generate logging code using the slf4j API.
 */
public class LargeJdbcSlf4jBenchmark extends AbstractLargeSampleBenchmark {

    @Override
    protected LoggingConfiguration loggingConfig() {
        return LoggingConfigurations.slf4j();
    }

}
