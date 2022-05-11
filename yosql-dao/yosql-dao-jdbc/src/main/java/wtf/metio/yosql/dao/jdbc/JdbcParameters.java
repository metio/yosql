/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.dao.jdbc;

import com.squareup.javapoet.ParameterSpec;
import wtf.metio.yosql.models.sql.ResultRowConverter;

public interface JdbcParameters {

    ParameterSpec dataSource();

    ParameterSpec connection();

    ParameterSpec preparedStatement();

    ParameterSpec resultSet();

    ParameterSpec resultSetMetaData();

    ParameterSpec columnCount();

    ParameterSpec index();

    ParameterSpec columnLabel();

    ParameterSpec converter(ResultRowConverter converter);

    Iterable<ParameterSpec> toMapConverterParameterSpecs();

}
