/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: CC0-1.0
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.exceptions.UnsupportedComponentTypeException;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ResultSetReaders")
class ResultSetReadersTest {

    private ResultSetReaders readers;

    @BeforeEach
    void setUp() {
        readers = new ResultSetReaders("resultSet");
    }

    private String read(final TypeName type, final String column, final String variable) {
        return readers.read(type, false, column, variable, variable).toString();
    }

    @Nested
    @DisplayName("object types")
    class Objects {

        @Test
        @DisplayName("reads a String")
        void string() {
            assertEquals("final java.lang.String slug = resultSet.getString(\"slug\");\n",
                    read(ClassName.get(String.class), "slug", "slug"));
        }

        @Test
        @DisplayName("reads a BigDecimal")
        void bigDecimal() {
            assertEquals("final java.math.BigDecimal price = resultSet.getBigDecimal(\"monthly_price\");\n",
                    read(ClassName.get(BigDecimal.class), "monthly_price", "price"));
        }

        @Test
        @DisplayName("reads a UUID through getObject, which answers null for NULL")
        void uuid() {
            assertEquals("final java.util.UUID id = resultSet.getObject(\"id\", java.util.UUID.class);\n",
                    read(ClassName.get(UUID.class), "id", "id"));
        }

        @Test
        @DisplayName("reads a LocalDate through getObject")
        void localDate() {
            assertTrue(read(ClassName.get(LocalDate.class), "due_on", "dueOn")
                    .contains("getObject(\"due_on\", java.time.LocalDate.class)"));
        }

        @Test
        @DisplayName("reads a byte array")
        void bytes() {
            assertEquals("final byte[] payload = resultSet.getBytes(\"payload\");\n",
                    read(ArrayTypeName.of(TypeName.BYTE), "payload", "payload"));
        }

    }

    @Nested
    @DisplayName("null handling")
    class Nulls {

        @Test
        @DisplayName("stops on a NULL where the component is a primitive")
        void primitiveRejectsNull() {
            final var code = read(TypeName.LONG, "id", "id");
            assertTrue(code.contains("final long id = resultSet.getLong(\"id\");"), code);
            assertTrue(code.contains("if (resultSet.wasNull())"), code);
            assertTrue(code.contains("throw new java.sql.SQLException"), code);
        }

        @Test
        @DisplayName("names the column and the component in that failure")
        void primitiveFailureNamesBoth() {
            final var code = readers.read(TypeName.LONG, false, "amount_cents", "amountMinorUnits",
                    "amount.minorUnits").toString();
            assertTrue(code.contains("amount_cents"), code);
            assertTrue(code.contains("amount.minorUnits"), code);
        }

        @Test
        @DisplayName("lets a boxed primitive be null")
        void boxedAcceptsNull() {
            final var code = read(TypeName.LONG.box(), "id", "id");
            assertEquals("final java.lang.Long id = resultSet.getObject(\"id\", java.lang.Long.class);\n", code);
            assertFalse(code.contains("wasNull"), code);
        }

        @Test
        @DisplayName("lets an Instant be null rather than the epoch")
        void instantAcceptsNull() {
            final var code = read(ClassName.get(Instant.class), "activated_at", "activatedAt");
            assertTrue(code.contains("final java.sql.Timestamp activatedAtTimestamp = "
                    + "resultSet.getTimestamp(\"activated_at\");"), code);
            assertTrue(code.contains("final java.time.Instant activatedAt = activatedAtTimestamp == null "
                    + "? null : activatedAtTimestamp.toInstant();"), code);
        }

        @Test
        @DisplayName("lets a Currency be null")
        void currencyAcceptsNull() {
            final var code = read(ClassName.get(Currency.class), "currency", "currency");
            assertTrue(code.contains("resultSet.getString(\"currency\")"), code);
            assertTrue(code.contains("currencyCode == null ? null : java.util.Currency.getInstance(currencyCode)"),
                    code);
        }

        @Test
        @DisplayName("lets an enum be null")
        void enumAcceptsNull() {
            final var state = ClassName.get("com.example", "OrderState");
            final var code = readers.read(state, true, "state", "state", "state").toString();
            assertTrue(code.contains("final java.lang.String stateName = resultSet.getString(\"state\");"), code);
            assertTrue(code.contains("stateName == null ? null : com.example.OrderState.valueOf(stateName)"), code);
        }

    }

    @Nested
    @DisplayName("primitives")
    class Primitives {

        @Test
        @DisplayName("uses the getter matching each primitive")
        void getters() {
            assertTrue(read(TypeName.BOOLEAN, "c", "v").contains("getBoolean"));
            assertTrue(read(TypeName.BYTE, "c", "v").contains("getByte"));
            assertTrue(read(TypeName.SHORT, "c", "v").contains("getShort"));
            assertTrue(read(TypeName.INT, "c", "v").contains("getInt"));
            assertTrue(read(TypeName.LONG, "c", "v").contains("getLong"));
            assertTrue(read(TypeName.FLOAT, "c", "v").contains("getFloat"));
            assertTrue(read(TypeName.DOUBLE, "c", "v").contains("getDouble"));
        }

        @Test
        @DisplayName("has nothing to read a char from")
        void chars() {
            assertThrows(UnsupportedComponentTypeException.class, () -> read(TypeName.CHAR, "c", "v"));
        }

    }

    @Nested
    @DisplayName("types it cannot read")
    class Unsupported {

        @Test
        @DisplayName("names the component, its type and what is supported")
        void diagnostic() {
            final var type = ClassName.get("com.example", "Address");
            final var exception = assertThrows(UnsupportedComponentTypeException.class,
                    () -> readers.read(type, false, "address", "address", "tenant.address"));
            assertTrue(exception.getMessage().contains("tenant.address"), exception.getMessage());
            assertTrue(exception.getMessage().contains("com.example.Address"), exception.getMessage());
            assertTrue(exception.getMessage().contains("BigDecimal"), exception.getMessage());
        }

        @Test
        @DisplayName("reports the same set through supports()")
        void supports() {
            assertTrue(readers.supports(TypeName.LONG, false));
            assertTrue(readers.supports(TypeName.LONG.box(), false));
            assertTrue(readers.supports(ClassName.get(String.class), false));
            assertTrue(readers.supports(ClassName.get(Instant.class), false));
            assertTrue(readers.supports(ClassName.get(UUID.class), false));
            assertTrue(readers.supports(ClassName.get(Currency.class), false));
            assertTrue(readers.supports(ClassName.get(BigDecimal.class), false));
            assertTrue(readers.supports(ArrayTypeName.of(TypeName.BYTE), false));
            assertTrue(readers.supports(ClassName.get("com.example", "OrderState"), true));
            assertFalse(readers.supports(ClassName.get("com.example", "Address"), false));
        }

    }

    @Test
    @DisplayName("reads from whatever the result set variable is called")
    void honoursTheVariableName() {
        final var renamed = new ResultSetReaders("row");
        assertEquals("final java.lang.String slug = row.getString(\"slug\");\n",
                renamed.read(ClassName.get(String.class), false, "slug", "slug", "slug").toString());
    }

}
