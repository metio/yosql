/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

/**
 * JMH based micro benchmark for YoSQL using the JDBC API and no logging output. It can be used as a baseline to measure
 * against in order to check code generation performance while using a large sample size.
 */
public class LargeJdbcNoOpBenchmark extends AbstractLargeSampleBenchmark {

    // default config already uses no-op logging

}
