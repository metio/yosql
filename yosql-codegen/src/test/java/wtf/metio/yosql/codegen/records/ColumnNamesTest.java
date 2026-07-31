/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ColumnNames")
class ColumnNamesTest {

    @ParameterizedTest
    @DisplayName("reads a component name as snake_case")
    @CsvSource({
            "id,               id",
            "slug,             slug",
            "tenantId,         tenant_id",
            "createdAt,        created_at",
            "accountId,        account_id",
            "timeZone,         time_zone",
            "minorUnits,       minor_units",
            "monthlyPrice,     monthly_price",
            "a,                a",
            "aB,               a_b",
    })
    void snakeCases(final String component, final String column) {
        assertEquals(column, ColumnNames.columnFor(component));
    }

    @ParameterizedTest
    @DisplayName("treats a run of capitals as one word")
    @CsvSource({
            "isVATRate,        is_vat_rate",
            "vatRate,          vat_rate",
            "httpURL,          http_url",
            "parseURLPath,     parse_url_path",
            "URL,              url",
            "URLPath,          url_path",
    })
    void acronyms(final String component, final String column) {
        assertEquals(column, ColumnNames.columnFor(component));
    }

    @ParameterizedTest
    @DisplayName("leaves a name that is already a column name alone")
    @CsvSource({
            "created_at,       created_at",
            "tenant_id,        tenant_id",
            "amount_cents,     amount_cents",
    })
    void alreadySnakeCase(final String component, final String column) {
        assertEquals(column, ColumnNames.columnFor(component));
    }

    @ParameterizedTest
    @DisplayName("keeps digits attached to the word they follow")
    @CsvSource({
            "line1,            line1",
            "address2Line,     address2_line",
            "iso4217Code,      iso4217_code",
    })
    void digits(final String component, final String column) {
        assertEquals(column, ColumnNames.columnFor(component));
    }

}
