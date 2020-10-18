/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.benchmark.full_lifecycle;

import org.openjdk.jmh.annotations.Setup;
import wtf.metio.yosql.DaggerYoSQLComponent;
import wtf.metio.yosql.model.configuration.RuntimeConfiguration;

import java.util.Locale;

abstract class AbstractYoSQLFullLifecycleBenchmark extends AbstractFullLifecycleBenchmark {

    @Setup
    public final void setUpYoSQL() {
        yosql = DaggerYoSQLComponent.builder()
                .locale(Locale.ENGLISH)
                .runtimeConfiguration(RuntimeConfiguration.usingDefaults())
                .build()
                .yosql();
    }

}
