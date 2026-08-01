/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.blocks;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;

import java.lang.reflect.Type;

public interface Fields {

    FieldSpec field(Type type, String name);

    FieldSpec field(TypeName type, String name);

    FieldSpec.Builder prepareConstant(Type type, String name);

    FieldSpec.Builder prepareConstant(TypeName type, String name);

    CodeBlock initialize(String statement);

}
