/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
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
