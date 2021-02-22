/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.codegen;

import wtf.metio.yosql.models.immutables.RuntimeConfiguration;

/**
 * JMH based micro benchmark for YoSQL using the default configuration.
 */
public class LargeSampleDefaultsBenchmark extends AbstractLargeSampleBenchmark {

    @Override
    protected RuntimeConfiguration runtimeConfiguration() {
        return RuntimeConfiguration.usingDefaults().build();
    }

}
