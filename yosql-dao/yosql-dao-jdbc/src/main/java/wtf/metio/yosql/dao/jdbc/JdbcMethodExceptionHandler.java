/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.codegen.api.MethodExceptionHandler;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.SQLException;
import java.util.Collections;

public final class JdbcMethodExceptionHandler implements MethodExceptionHandler {

    @Override
    public Iterable<? extends TypeName> thrownExceptions(final SqlConfiguration configuration) {
        return configuration.catchAndRethrow()
                .filter(Boolean.TRUE::equals)
                .map(bool -> Collections.<TypeName>emptyList())
                .orElseGet(() -> Collections.singletonList(ClassName.get(SQLException.class)));
    }

}
