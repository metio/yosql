/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;

import java.util.List;

/**
 * A type as it appears in a source file, read far enough to build a converter for it.
 *
 * <p>Only the shape a result-row converter needs is kept: whether the type is a record, an enum or
 * neither, and — for a record — the components of its canonical constructor in declaration order.</p>
 */
public record JavaSourceType(ClassName type, Kind kind, List<JavaSourceComponent> components) {

    public enum Kind {
        RECORD,
        ENUM,
        OTHER
    }

    public static JavaSourceType record(final ClassName type, final List<JavaSourceComponent> components) {
        return new JavaSourceType(type, Kind.RECORD, List.copyOf(components));
    }

    public static JavaSourceType enumeration(final ClassName type) {
        return new JavaSourceType(type, Kind.ENUM, List.of());
    }

    public static JavaSourceType other(final ClassName type) {
        return new JavaSourceType(type, Kind.OTHER, List.of());
    }

    public boolean isRecord() {
        return kind == Kind.RECORD;
    }

    public boolean isEnum() {
        return kind == Kind.ENUM;
    }

}
