/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.benchmarks.common.jdbc;

import wtf.metio.yosql.benchmark.common.persistence.CompanyRepository;
import wtf.metio.yosql.benchmarks.common.ReadSingleEntityByPrimaryKey;

public class JdbcReadSingleEntityByPrimaryKey implements ReadSingleEntityByPrimaryKey {

    private final CompanyRepository companyRepository;

    public JdbcReadSingleEntityByPrimaryKey(final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void readSingleEntityByPrimaryKey() {
        companyRepository.findCompany(1);
    }

}
