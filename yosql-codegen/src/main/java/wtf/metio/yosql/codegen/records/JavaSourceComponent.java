/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.TypeName;

/**
 * One component of a record's canonical constructor: the name it was declared with and the type it
 * was declared as, resolved to a fully-qualified name.
 */
public record JavaSourceComponent(String name, TypeName type) {
}
