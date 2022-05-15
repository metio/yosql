/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.models.immutables.LoggingConfiguration;
import wtf.metio.yosql.testing.configs.LoggingConfigurations;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and log4j as logging implementation using a medium
 * sample size of repositories. It can be compared against the no-op implementation to check how much extra time is
 * spent by YoSQL to generate logging code using the log4j API.
 */
public class MediumJdbcLog4jBenchmark extends AbstractMediumSampleBenchmark {

    @Override
    protected LoggingConfiguration loggingConfig() {
        return LoggingConfigurations.log4j();
    }

}
