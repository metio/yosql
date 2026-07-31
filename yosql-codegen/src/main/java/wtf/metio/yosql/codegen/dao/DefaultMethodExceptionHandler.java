/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.SQLException;
import java.util.Collections;

public final class DefaultMethodExceptionHandler implements MethodExceptionHandler {

    @Override
    public Iterable<? extends TypeName> thrownExceptions(final SqlConfiguration configuration) {
        return configuration.catchAndRethrow()
                .filter(Boolean.TRUE::equals)
                .map(bool -> Collections.<TypeName>emptyList())
                .orElseGet(() -> Collections.singletonList(thrownException()));
    }

    @Override
    public TypeName thrownException() {
        return ClassName.get(SQLException.class);
    }

}
