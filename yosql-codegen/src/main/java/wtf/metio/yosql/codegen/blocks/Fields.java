/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.blocks;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import java.lang.reflect.Type;

public interface Fields {

    FieldSpec field(Type type, String name);

    FieldSpec field(TypeName type, String name);

    FieldSpec.Builder prepareConstant(Type type, String name);

    FieldSpec.Builder prepareConstant(TypeName type, String name);

    CodeBlock initialize(String statement);

}
