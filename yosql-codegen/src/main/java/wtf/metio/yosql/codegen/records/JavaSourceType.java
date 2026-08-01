/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.List;

/**
 * A type as it appears in a source file, read far enough to build a converter for it.
 *
 * <p>Only the shape a result-row converter needs is kept: whether the type is a record, an enum or
 * neither; for a record, the components of its canonical constructor in declaration order; and for
 * any of them, the parameter types of a {@code static valueOf} factory returning the type.</p>
 *
 * <p>That factory is what lets a type be read from a single column when nothing else could read it.
 * It also settles what a one-component record means: with a factory it is a value wrapped around a
 * column, without one it is a record whose component reads a column of its own.</p>
 */
public record JavaSourceType(
        ClassName type,
        Kind kind,
        List<JavaSourceComponent> components,
        List<TypeName> valueOfParameters,
        List<JavaSourceMethod> resultSetMethods) {

    public enum Kind {
        RECORD,
        ENUM,
        OTHER
    }

    public static JavaSourceType record(
            final ClassName type,
            final List<JavaSourceComponent> components,
            final List<TypeName> valueOfParameters,
            final List<JavaSourceMethod> resultSetMethods) {
        return new JavaSourceType(type, Kind.RECORD, List.copyOf(components),
                List.copyOf(valueOfParameters), List.copyOf(resultSetMethods));
    }

    public static JavaSourceType enumeration(final ClassName type) {
        return new JavaSourceType(type, Kind.ENUM, List.of(), List.of(), List.of());
    }

    public static JavaSourceType other(
            final ClassName type,
            final List<TypeName> valueOfParameters,
            final List<JavaSourceMethod> resultSetMethods) {
        return new JavaSourceType(type, Kind.OTHER, List.of(),
                List.copyOf(valueOfParameters), List.copyOf(resultSetMethods));
    }

    public boolean isRecord() {
        return kind == Kind.RECORD;
    }

    public boolean isEnum() {
        return kind == Kind.ENUM;
    }

    /**
     * @return whether the type declares a factory taking a single value, and can therefore be built
     *         from one column
     */
    public boolean hasValueOf() {
        return !valueOfParameters.isEmpty();
    }

}
