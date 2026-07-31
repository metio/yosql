/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

public interface Classes {

    TypeSpec.Builder publicInterface(ClassName name);

    TypeSpec.Builder publicClass(ClassName name);

    TypeSpec.Builder openClass(ClassName name);

}
