/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ParameterSpec;
import wtf.metio.yosql.models.configuration.ResultRowConverter;

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
