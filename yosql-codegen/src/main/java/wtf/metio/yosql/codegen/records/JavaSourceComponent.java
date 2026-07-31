/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.TypeName;

/**
 * One component of a record's canonical constructor: the name it was declared with and the type it
 * was declared as, resolved to a fully-qualified name.
 */
public record JavaSourceComponent(String name, TypeName type) {
}
