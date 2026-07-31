/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ParameterSpec;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

public interface ParameterGenerator {

    Iterable<ParameterSpec> asParameterSpecs(SqlConfiguration configuration);

    Iterable<ParameterSpec> asParameterSpecsForInterfaces(SqlConfiguration configuration);

    Iterable<ParameterSpec> asBatchParameterSpecs(SqlConfiguration configuration);

    Iterable<ParameterSpec> asBatchParameterSpecsForInterfaces(SqlConfiguration configuration);

}
