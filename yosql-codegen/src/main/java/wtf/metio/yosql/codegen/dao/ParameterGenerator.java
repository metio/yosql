/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
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
