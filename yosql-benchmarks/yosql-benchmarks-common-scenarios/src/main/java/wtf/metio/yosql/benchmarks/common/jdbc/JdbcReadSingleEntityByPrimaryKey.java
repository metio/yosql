/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmarks.common.jdbc;

import org.openjdk.jmh.annotations.Benchmark;
import wtf.metio.yosql.benchmarks.common.AbstractBenchmark;
import wtf.metio.yosql.benchmarks.common.ReadSingleEntityByPrimaryKey;

public final class JdbcReadSingleEntityByPrimaryKey extends AbstractBenchmark implements ReadSingleEntityByPrimaryKey {

    @Override
    @Benchmark
    public void readSingleEntityByPrimaryKey() {
        companyRepository.findCompany(1);
    }

}
