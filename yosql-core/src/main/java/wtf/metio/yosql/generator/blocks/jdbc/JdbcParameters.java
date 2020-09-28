/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.generator.blocks.jdbc;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public interface JdbcParameters {

    ParameterSpec dataSource();

    ParameterSpec connection();

    ParameterSpec preparedStatement();

    ParameterSpec resultSet();

    ParameterSpec metaData();

    ParameterSpec columnCount();

    ParameterSpec index();

    ParameterSpec columnLabel();

}
