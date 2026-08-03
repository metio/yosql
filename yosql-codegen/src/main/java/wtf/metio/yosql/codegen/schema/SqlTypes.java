/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.schema;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * What a column's SQL type means in Java.
 *
 * <p>A type this does not recognise is *unknown*, never {@code Object}. Falling back to
 * {@code Object} would produce a method signature that compiles and accepts anything, which is the
 * failure this whole feature exists to prevent — so a type nobody mapped simply leaves the
 * generator to whatever it would have done without a catalog.</p>
 *
 * <p>Databases spell the same type differently, and some spell different types the same. The base
 * map is standard SQL plus the spellings almost everyone shares; a vendor adds or overrides on top,
 * keyed by the name the JDBC driver reports, which is the same name a statement's
 * {@code vendor} is matched against.</p>
 *
 * <p>Nullability decides boxing. A {@code not null} column becomes a primitive where one exists,
 * because it cannot hold a null and a primitive says so; a nullable column becomes the boxed type,
 * because the alternative is a silent unboxing failure on the first null row.</p>
 */
public final class SqlTypes {

    private static final ClassName STRING = ClassName.get(String.class);
    private static final ClassName BIG_DECIMAL = ClassName.get(BigDecimal.class);
    private static final ClassName INSTANT = ClassName.get(Instant.class);
    private static final ClassName LOCAL_DATE = ClassName.get(LocalDate.class);
    private static final ClassName LOCAL_TIME = ClassName.get(LocalTime.class);
    private static final ClassName LOCAL_DATE_TIME = ClassName.get(LocalDateTime.class);
    private static final ClassName UUID_TYPE = ClassName.get(UUID.class);
    private static final TypeName BYTES = ArrayTypeName.of(TypeName.BYTE);

    /**
     * The spellings every database in this list shares. Where a vendor disagrees it says so below
     * rather than here.
     */
    private static final Map<String, TypeName> STANDARD = Map.ofEntries(
            Map.entry("char", STRING),
            Map.entry("character", STRING),
            Map.entry("varchar", STRING),
            Map.entry("character varying", STRING),
            Map.entry("nchar", STRING),
            Map.entry("nvarchar", STRING),
            Map.entry("text", STRING),
            Map.entry("clob", STRING),
            Map.entry("smallint", TypeName.SHORT),
            Map.entry("int", TypeName.INT),
            Map.entry("integer", TypeName.INT),
            Map.entry("bigint", TypeName.LONG),
            Map.entry("boolean", TypeName.BOOLEAN),
            Map.entry("bool", TypeName.BOOLEAN),
            Map.entry("real", TypeName.FLOAT),
            Map.entry("double precision", TypeName.DOUBLE),
            Map.entry("double", TypeName.DOUBLE),
            Map.entry("float", TypeName.DOUBLE),
            Map.entry("numeric", BIG_DECIMAL),
            Map.entry("decimal", BIG_DECIMAL),
            Map.entry("dec", BIG_DECIMAL),
            Map.entry("date", LOCAL_DATE),
            Map.entry("time", LOCAL_TIME),
            Map.entry("timestamp", LOCAL_DATE_TIME),
            Map.entry("timestamp with time zone", INSTANT),
            Map.entry("timestamp without time zone", LOCAL_DATE_TIME),
            Map.entry("uuid", UUID_TYPE),
            Map.entry("blob", BYTES),
            Map.entry("binary", BYTES),
            Map.entry("varbinary", BYTES));

    private static final Map<String, Map<String, TypeName>> BY_VENDOR = Map.of(
            "postgresql", Map.ofEntries(
                    Map.entry("int2", TypeName.SHORT),
                    Map.entry("int4", TypeName.INT),
                    Map.entry("int8", TypeName.LONG),
                    Map.entry("serial", TypeName.INT),
                    Map.entry("serial4", TypeName.INT),
                    Map.entry("bigserial", TypeName.LONG),
                    Map.entry("serial8", TypeName.LONG),
                    Map.entry("smallserial", TypeName.SHORT),
                    Map.entry("float4", TypeName.FLOAT),
                    Map.entry("float8", TypeName.DOUBLE),
                    Map.entry("timestamptz", INSTANT),
                    Map.entry("timetz", LOCAL_TIME),
                    Map.entry("bytea", BYTES),
                    Map.entry("json", STRING),
                    Map.entry("jsonb", STRING),
                    Map.entry("citext", STRING)),
            "mysql", Map.ofEntries(
                    Map.entry("tinyint", TypeName.BYTE),
                    Map.entry("mediumint", TypeName.INT),
                    Map.entry("datetime", LOCAL_DATE_TIME),
                    Map.entry("tinytext", STRING),
                    Map.entry("mediumtext", STRING),
                    Map.entry("longtext", STRING),
                    Map.entry("tinyblob", BYTES),
                    Map.entry("mediumblob", BYTES),
                    Map.entry("longblob", BYTES),
                    Map.entry("json", STRING)),
            "mariadb", Map.of(
                    "tinyint", TypeName.BYTE,
                    "datetime", LOCAL_DATE_TIME,
                    "longtext", STRING,
                    "json", STRING),
            "h2", Map.of(
                    "identity", TypeName.LONG,
                    "varchar_ignorecase", STRING,
                    "json", STRING),
            "oracle", Map.ofEntries(
                    Map.entry("varchar2", STRING),
                    Map.entry("nvarchar2", STRING),
                    Map.entry("number", BIG_DECIMAL),
                    Map.entry("binary_float", TypeName.FLOAT),
                    Map.entry("binary_double", TypeName.DOUBLE),
                    Map.entry("raw", BYTES),
                    Map.entry("long raw", BYTES)),
            "microsoft sql server", Map.ofEntries(
                    Map.entry("tinyint", TypeName.SHORT),
                    Map.entry("datetime", LOCAL_DATE_TIME),
                    Map.entry("datetime2", LOCAL_DATE_TIME),
                    Map.entry("datetimeoffset", INSTANT),
                    Map.entry("smalldatetime", LOCAL_DATE_TIME),
                    Map.entry("money", BIG_DECIMAL),
                    Map.entry("smallmoney", BIG_DECIMAL),
                    Map.entry("uniqueidentifier", UUID_TYPE),
                    Map.entry("nvarchar(max)", STRING),
                    Map.entry("bit", TypeName.BOOLEAN)));

    private SqlTypes() {
        // utility class, call #javaType() directly
    }

    /**
     * @param column the column as its DDL declared it
     * @param vendor the database the statement is written for, as a JDBC driver reports it
     * @return the Java type that column reads into, or empty when nothing here recognises it
     */
    public static Optional<TypeName> javaType(final Column column, final Optional<String> vendor) {
        return lookup(column.baseType(), vendor)
                .map(type -> column.nullable() ? box(type) : type);
    }

    private static Optional<TypeName> lookup(final String baseType, final Optional<String> vendor) {
        return vendor.map(name -> name.toLowerCase(Locale.ROOT).strip())
                .map(BY_VENDOR::get)
                .map(types -> types.get(baseType))
                .or(() -> Optional.ofNullable(STANDARD.get(baseType)));
    }

    /**
     * A nullable column has to read into something that can hold a null. An unboxed primitive would
     * throw on the first row that has one, at run time, in whichever query hit it first.
     */
    private static TypeName box(final TypeName type) {
        return type.isPrimitive() ? type.box() : type;
    }

}
