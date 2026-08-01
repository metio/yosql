/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeSpec;

public interface Classes {

    TypeSpec.Builder publicInterface(ClassName name);

    TypeSpec.Builder publicClass(ClassName name);

    TypeSpec.Builder openClass(ClassName name);

}
