/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

public interface Parameters {

    ParameterSpec parameter(Class<?> type, String name);

    ParameterSpec parameter(TypeName type, String name);

    ParameterSpec parameterForInterfaces(TypeName type, String name);

}
