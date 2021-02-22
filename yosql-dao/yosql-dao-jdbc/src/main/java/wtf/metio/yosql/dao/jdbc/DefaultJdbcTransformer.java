/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.SQLException;
import java.util.Collections;

public final class DefaultJdbcTransformer implements JdbcTransformer {

    @Override
    public Iterable<TypeName> sqlException(final SqlConfiguration configuration) {
        if (!configuration.catchAndRethrow()) {
            return Collections.singletonList(ClassName.get(SQLException.class));
        }
        return Collections.emptyList();
    }

}
