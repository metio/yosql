/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.dao.ebean;

import com.squareup.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.api.MutinyMethodGenerator;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;

public final class EBeanMutinyMethodGenerator implements MutinyMethodGenerator {

    @Override
    public MethodSpec mutinyReadMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec mutinyWriteMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

    @Override
    public MethodSpec mutinyCallMethod(final SqlConfiguration configuration, final List<SqlStatement> vendorStatements) {
        return null;
    }

}