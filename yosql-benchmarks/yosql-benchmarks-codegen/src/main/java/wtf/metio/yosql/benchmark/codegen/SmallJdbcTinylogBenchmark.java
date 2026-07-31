/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.internals.testing.configs.LoggingConfigurations;
import wtf.metio.yosql.models.immutables.LoggingConfiguration;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and tinylog as logging implementation using a small
 * sample size of repositories. It can be compared against the no-op implementation to check how much extra time is
 * spent by YoSQL to generate logging code using the tinylog API.
 */
public class SmallJdbcTinylogBenchmark extends AbstractSmallSampleBenchmark {

    @Override
    protected LoggingConfiguration loggingConfig() {
        return LoggingConfigurations.tiny();
    }

}
