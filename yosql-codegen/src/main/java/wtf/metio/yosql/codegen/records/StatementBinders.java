/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Currency;
import java.util.Optional;

/**
 * Turns a value of a declared parameter type into something a driver will accept.
 *
 * <p>The inverse of {@link ResultSetReaders}, and needed for the same reason: what a driver hands
 * back and what it takes are not the same set. {@code getTimestamp} answers a value an
 * {@link Instant} is built from, but {@code setObject} refuses an {@code Instant} outright —
 * PostgreSQL replies "Can't infer the SQL type to use". A parameter declared as the type the domain
 * actually uses would otherwise compile and fail at runtime.</p>
 *
 * <p>Types a driver already accepts are left exactly as they are, so a statement whose parameters
 * need no conversion generates the code it always generated.</p>
 */
public final class StatementBinders {

    private static final ClassName INSTANT_TYPE = ClassName.get(Instant.class);
    private static final ClassName CURRENCY_TYPE = ClassName.get(Currency.class);
    private static final ClassName TIMESTAMP_TYPE = ClassName.get(Timestamp.class);

    /**
     * @param type the declared type of the parameter
     * @param enumeration whether that type is an enum, which is written as text
     * @param value an expression of that type
     * @return what to bind instead of {@code value}, or empty when the driver accepts it as it is
     */
    public Optional<CodeBlock> bind(final TypeName type, final boolean enumeration, final CodeBlock value) {
        if (enumeration) {
            return Optional.of(CodeBlock.of("$L.name()", value));
        }
        if (INSTANT_TYPE.equals(type)) {
            return Optional.of(CodeBlock.of("$T.from($L)", TIMESTAMP_TYPE, value));
        }
        if (CURRENCY_TYPE.equals(type)) {
            return Optional.of(CodeBlock.of("$L.getCurrencyCode()", value));
        }
        return Optional.empty();
    }

    /**
     * @return the type the bound expression has, which is what a hoisted local is declared as
     */
    public TypeName boundType(final TypeName type, final boolean enumeration) {
        if (enumeration) {
            return ClassName.get(String.class);
        }
        if (INSTANT_TYPE.equals(type)) {
            return TIMESTAMP_TYPE;
        }
        if (CURRENCY_TYPE.equals(type)) {
            return ClassName.get(String.class);
        }
        return type.isPrimitive() ? type.box() : type;
    }

    /**
     * @return whether binding {@code type} would dereference the value, and therefore needs a null
     *         check in front of it
     */
    public boolean dereferences(final TypeName type, final boolean enumeration) {
        return enumeration || CURRENCY_TYPE.equals(type) || INSTANT_TYPE.equals(type);
    }

}
