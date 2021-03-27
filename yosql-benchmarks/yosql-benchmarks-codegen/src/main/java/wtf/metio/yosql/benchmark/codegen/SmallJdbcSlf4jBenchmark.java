/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.models.constants.api.LoggingApis;
import wtf.metio.yosql.models.immutables.ApiConfiguration;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and slf4j as logging implementation using a small
 * sample size of repositories. It can be compared against the no-op implementation to check how much extra time is
 * spent by YoSQL to generate logging code using the slf4j API.
 */
public class SmallJdbcSlf4jBenchmark extends AbstractSmallSampleBenchmark {

    @Override
    protected ApiConfiguration apiConfig() {
        return ApiConfiguration.usingDefaults().setLoggingApi(LoggingApis.SLF4J).build();
    }

}
