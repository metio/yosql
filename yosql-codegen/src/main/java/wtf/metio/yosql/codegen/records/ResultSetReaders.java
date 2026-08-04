/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;
import wtf.metio.yosql.codegen.exceptions.UnsupportedComponentTypeException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Turns a component type into the straight-line JDBC that reads it.
 *
 * <p>Every case emits a local variable declaration and nothing else: the calls a careful person
 * would have written by hand, resolved here instead of at runtime. Nothing is looked up, boxed
 * through {@code Object}, or dispatched on a type token, so a native image has nothing left to
 * need a hint for.</p>
 *
 * <p>Null is the case worth stating. {@code getLong} answers {@code 0} for SQL NULL and
 * {@code getInt} answers {@code 0}, which is how a nullable column silently becomes a wrong number.
 * A primitive component therefore reads the value and then checks {@link java.sql.ResultSet#wasNull()},
 * failing with the column's name; a boxed or object component reads through
 * {@code getObject(column, Type.class)} or an explicit null check, and arrives as {@code null}.</p>
 */
public final class ResultSetReaders {

    /**
     * What a reader appends to a component's local when it needs a second one beside it — the raw
     * column before a factory turns it into the component's type, the timestamp before it becomes an
     * {@code Instant}, the name or ordinal before it becomes an enum constant.
     *
     * <p>Public because the caller choosing the component's local is the only place that can keep
     * these clear of each other: a record holding both {@code reason} and {@code reasonName} would
     * otherwise declare {@code reasonName} twice, and the converter would not compile.</p>
     */
    public static final Set<String> DERIVED_SUFFIXES = Set.of("Value", "Timestamp", "Code", "Name");

    private static final ClassName UUID_TYPE = ClassName.get(UUID.class);
    private static final ClassName INSTANT_TYPE = ClassName.get(Instant.class);
    private static final ClassName BIG_DECIMAL_TYPE = ClassName.get(BigDecimal.class);
    private static final ClassName STRING_TYPE = ClassName.get(String.class);
    private static final ClassName TIMESTAMP_TYPE = ClassName.get(Timestamp.class);
    private static final TypeName BYTE_ARRAY = ArrayTypeName.of(TypeName.BYTE);

    private static final List<ClassName> VIA_GET_OBJECT = List.of(
            UUID_TYPE,
            ClassName.get(LocalDate.class),
            ClassName.get(LocalDateTime.class),
            ClassName.get(LocalTime.class),
            ClassName.get(OffsetDateTime.class));

    private static final ClassName CURRENCY_TYPE = ClassName.get(Currency.class);

    private static final String SUPPORTED = "String, UUID, Instant, LocalDate, LocalDateTime, "
            + "LocalTime, OffsetDateTime, BigDecimal, Currency, byte[], the primitives and their wrappers";

    private final String resultSet;

    public ResultSetReaders(final String resultSetVariable) {
        this.resultSet = resultSetVariable;
    }

    /**
     * @param type the component's declared type
     * @param enumeration whether that type is an enum, which is read as text
     * @param column the column to read
     * @param variable the local to declare
     * @param path the component's path within the result row type, for diagnostics
     * @return the declaration of {@code variable}
     */
    public CodeBlock read(
            final TypeName type,
            final boolean enumeration,
            final String column,
            final String variable,
            final String path) {
        return read(type, enumeration, CodeBlock.of("$S", column), column, variable, path);
    }

    /**
     * Reads the first column, for a statement whose result is a single value rather than a row.
     *
     * <p>By position rather than by name, because a value has no name to go by: {@code select
     * count(*)} names nothing at all, and inventing an alias requirement for it would be a rule
     * about SQL style rather than about mapping.</p>
     */
    public CodeBlock readFirstColumn(
            final TypeName type,
            final boolean enumeration,
            final String variable,
            final String path) {
        return read(type, enumeration, CodeBlock.of("1"), "1", variable, path);
    }

    private CodeBlock read(
            final TypeName type,
            final boolean enumeration,
            final CodeBlock column,
            final String described,
            final String variable,
            final String path) {
        if (enumeration) {
            return readEnum(type, column, variable);
        }
        if (type.isPrimitive()) {
            return readPrimitive(type, column, described, variable, path);
        }
        if (type.isBoxedPrimitive()) {
            return declare(type, variable, CodeBlock.of("$N.getObject($L, $T.class)", resultSet, column, type));
        }
        if (INSTANT_TYPE.equals(type)) {
            return readInstant(column, variable);
        }
        if (STRING_TYPE.equals(type)) {
            return declare(type, variable, CodeBlock.of("$N.getString($L)", resultSet, column));
        }
        if (BIG_DECIMAL_TYPE.equals(type)) {
            return declare(type, variable, CodeBlock.of("$N.getBigDecimal($L)", resultSet, column));
        }
        if (BYTE_ARRAY.equals(type)) {
            return declare(type, variable, CodeBlock.of("$N.getBytes($L)", resultSet, column));
        }
        if (CURRENCY_TYPE.equals(type)) {
            return readCurrency(column, variable);
        }
        if (VIA_GET_OBJECT.contains(type)) {
            return declare(type, variable, CodeBlock.of("$N.getObject($L, $T.class)", resultSet, column, type));
        }
        throw new UnsupportedComponentTypeException(path, type, SUPPORTED);
    }

    public boolean supports(final TypeName type, final boolean enumeration) {
        return enumeration
                || type.isPrimitive()
                || type.isBoxedPrimitive()
                || INSTANT_TYPE.equals(type)
                || STRING_TYPE.equals(type)
                || BIG_DECIMAL_TYPE.equals(type)
                || BYTE_ARRAY.equals(type)
                || CURRENCY_TYPE.equals(type)
                || VIA_GET_OBJECT.contains(type);
    }

    /**
     * Reads one column and hands it to the type's own {@code valueOf}.
     *
     * <p>The column is read exactly as it would be for a bare value of the parameter's type, so
     * every rule above still holds — a primitive parameter refuses NULL, an object parameter accepts
     * it — and the factory is called only for a value that is there. A type that can say how to
     * build itself from a column needs no configuration to say it.</p>
     */
    public CodeBlock readVia(
            final TypeName valueType,
            final TypeName parameterType,
            final String column,
            final String variable,
            final String path) {
        return readVia(valueType, parameterType, read(parameterType, false, column, variable + "Value", path),
                variable);
    }

    /**
     * The same, for a single value read by position.
     */
    public CodeBlock readFirstColumnVia(
            final TypeName valueType,
            final TypeName parameterType,
            final String variable,
            final String path) {
        return readVia(valueType, parameterType,
                readFirstColumn(parameterType, false, variable + "Value", path), variable);
    }

    private CodeBlock readVia(
            final TypeName valueType,
            final TypeName parameterType,
            final CodeBlock read,
            final String variable) {
        final var raw = variable + "Value";
        final var block = CodeBlock.builder().add(read);
        if (parameterType.isPrimitive()) {
            block.addStatement("final $T $N = $T.valueOf($N)", valueType, variable, valueType, raw);
        } else {
            block.addStatement("final $T $N = $N == null ? null : $T.valueOf($N)",
                    valueType, variable, raw, valueType, raw);
        }
        return block.build();
    }

    private CodeBlock readPrimitive(
            final TypeName type, final CodeBlock column, final String described,
            final String variable, final String path) {
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.$N($L)", type, variable, resultSet, primitiveGetter(type, path), column)
                .beginControlFlow("if ($N.wasNull())", resultSet)
                // A primitive cannot hold NULL, and answering 0 or false instead is the bug this
                // check exists to make impossible.
                .addStatement("throw new $T($S)", SQLException.class,
                        "Column '%s' is NULL, but component '%s' is a %s and cannot represent it"
                                .formatted(described, path, type))
                .endControlFlow()
                .build();
    }

    /**
     * {@code getTimestamp} rather than {@code getObject(column, Instant.class)}: the latter is not
     * something every driver implements, while a {@link Timestamp} holds the epoch millisecond and
     * converts exactly.
     */
    private CodeBlock readInstant(final CodeBlock column, final String variable) {
        final var raw = variable + "Timestamp";
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getTimestamp($L)", TIMESTAMP_TYPE, raw, resultSet, column)
                .addStatement("final $T $N = $N == null ? null : $N.toInstant()", INSTANT_TYPE, variable, raw, raw)
                .build();
    }

    /**
     * No driver returns a {@link Currency}, but its ISO-4217 code is exactly what the column holds,
     * and it is the other half of every money value object.
     */
    private CodeBlock readCurrency(final CodeBlock column, final String variable) {
        final var raw = variable + "Code";
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getString($L)", STRING_TYPE, raw, resultSet, column)
                .addStatement("final $T $N = $N == null ? null : $T.getInstance($N)",
                        CURRENCY_TYPE, variable, raw, CURRENCY_TYPE, raw)
                .build();
    }

    private CodeBlock readEnum(final TypeName type, final CodeBlock column, final String variable) {
        final var raw = variable + "Name";
        return CodeBlock.builder()
                .addStatement("final $T $N = $N.getString($L)", STRING_TYPE, raw, resultSet, column)
                // valueOf throws IllegalArgumentException naming the type and the value it did not
                // know. A persistence layer handed a state it cannot represent should stop, not
                // invent a default.
                .addStatement("final $T $N = $N == null ? null : $T.valueOf($N)", type, variable, raw, type, raw)
                .build();
    }

    private static CodeBlock declare(final TypeName type, final String variable, final CodeBlock value) {
        return CodeBlock.builder().addStatement("final $T $N = $L", type, variable, value).build();
    }

    private static String primitiveGetter(final TypeName type, final String path) {
        if (TypeName.BOOLEAN.equals(type)) {
            return "getBoolean";
        } else if (TypeName.BYTE.equals(type)) {
            return "getByte";
        } else if (TypeName.SHORT.equals(type)) {
            return "getShort";
        } else if (TypeName.INT.equals(type)) {
            return "getInt";
        } else if (TypeName.LONG.equals(type)) {
            return "getLong";
        } else if (TypeName.FLOAT.equals(type)) {
            return "getFloat";
        } else if (TypeName.DOUBLE.equals(type)) {
            return "getDouble";
        }
        throw new UnsupportedComponentTypeException(path, type, SUPPORTED);
    }

}
