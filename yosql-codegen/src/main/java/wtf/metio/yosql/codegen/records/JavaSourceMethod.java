/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.TypeName;

/**
 * A method that turns a result set row into something, as declared in a source file.
 */
public record JavaSourceMethod(String name, TypeName returnType) {
}
