/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.fluentjdbc;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.ReactorMethodGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class FluentJdbcReactorMethodGenerator implements ReactorMethodGenerator {

    @Override
    public MethodSpec reactorCallMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec reactorReadMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec reactorWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

}