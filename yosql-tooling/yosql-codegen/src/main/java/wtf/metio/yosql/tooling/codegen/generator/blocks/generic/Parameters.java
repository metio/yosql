/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.tooling.codegen.generator.blocks.generic;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import wtf.metio.yosql.tooling.codegen.model.sql.SqlParameter;

import java.util.List;

public interface Parameters {

    ParameterSpec parameter(Class<?> type, String name);
    ParameterSpec parameter(TypeName type, String name);

    Iterable<ParameterSpec> asParameterSpecs(List<SqlParameter> parameters);
    Iterable<ParameterSpec> asBatchParameterSpecs(List<SqlParameter> parameters);

    Iterable<ParameterSpec> resultState(TypeName type);

}
