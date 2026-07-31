/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.records;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.internals.testing.configs.ConverterConfigurations;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("RecordConverterNames")
class RecordConverterNamesTest {

    private static final ClassName TENANT = ClassName.get("com.example.domain", "Tenant");

    private final RecordConverterNames names =
            new RecordConverterNames(ConverterConfigurations.withConverters());

    @Test
    @DisplayName("puts a converter beside the map converter")
    void converterPackageFollowsTheMapConverter() {
        assertEquals(ClassName.get("com.example.persistence.converter", "ToTenantConverter"),
                names.converterClass(TENANT));
    }

    @Test
    @DisplayName("follows the map converter wherever it was configured to live")
    void converterPackageIsConfigurable() {
        final var relocated = ConverterConfiguration.copyOf(ConverterConfigurations.withoutConverters())
                .withMapConverterClass("your.own.domain.mapping.ToMapConverter");
        assertEquals(ClassName.get("your.own.domain.mapping", "ToTenantConverter"),
                new RecordConverterNames(relocated).converterClass(TENANT));
    }

    @Test
    @DisplayName("names the repository field after the record")
    void alias() {
        assertEquals("tenantConverter", names.alias(TENANT));
        assertEquals("ledgerEntryConverter", names.alias(ClassName.get("com.example", "LedgerEntry")));
    }

    @Test
    @DisplayName("calls every generated converter the same thing")
    void methodName() {
        assertEquals("asUserType", names.methodName());
    }

    @Test
    @DisplayName("takes the class-name affixes from configuration")
    void affixesAreConfigurable() {
        final var renamed = ConverterConfiguration.copyOf(ConverterConfigurations.withConverters())
                .withRecordConverterPrefix("")
                .withRecordConverterSuffix("RowMapper");
        final var names = new RecordConverterNames(renamed);
        assertEquals(ClassName.get("com.example.persistence.converter", "TenantRowMapper"),
                names.converterClass(TENANT));
        assertEquals("tenantRowMapper", names.alias(TENANT), "the field follows the suffix");
    }

    @Test
    @DisplayName("takes the method name from configuration")
    void methodNameIsConfigurable() {
        final var renamed = ConverterConfiguration.copyOf(ConverterConfigurations.withConverters())
                .withRecordConverterMethod("mapRow");
        assertEquals("mapRow", new RecordConverterNames(renamed).methodName());
    }

    @Test
    @DisplayName("uses only the simple name, so two packages cannot both be right")
    void nestedTypesUseTheirOwnSimpleName() {
        // A nested record is still addressed by its simple name here; two records sharing one
        // would collide, and the generator is where that is caught.
        assertEquals(ClassName.get("com.example.persistence.converter", "ToMoneyConverter"),
                names.converterClass(ClassName.get("com.example.domain", "Holder", "Money")));
    }

}
