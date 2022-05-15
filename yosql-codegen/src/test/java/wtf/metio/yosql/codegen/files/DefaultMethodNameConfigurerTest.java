/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.logging.LoggingObjectMother;
import wtf.metio.yosql.models.configuration.SqlType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.testing.configs.RepositoriesConfigurations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultMethodNameConfigurer")
class DefaultMethodNameConfigurerTest {

    private DefaultMethodNameConfigurer configurer;
    private RepositoriesConfiguration repositories;

    @BeforeEach
    void setUp() {
        repositories = RepositoriesConfigurations.defaults();
        configurer = new DefaultMethodNameConfigurer(LoggingObjectMother.logger(), repositories);
    }

    @Test
    void batchNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBatchPrefix("prefix").build();
        final var adapted = configurer.batchNamePrefix(original);
        assertEquals(original.batchPrefix(), adapted.batchPrefix());
    }

    @Test
    void batchNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setBatchPrefix(" ").build();
        final var adapted = configurer.batchNamePrefix(original);
        assertEquals(repositories.batchPrefix(), adapted.batchPrefix());
    }

    @Test
    void batchNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBatchSuffix("suffix").build();
        final var adapted = configurer.batchNameSuffix(original);
        assertEquals(original.batchSuffix(), adapted.batchSuffix());
    }

    @Test
    void batchNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setBatchSuffix(" ").build();
        final var adapted = configurer.batchNameSuffix(original);
        assertEquals(repositories.batchSuffix(), adapted.batchSuffix());
    }

    @Test
    void blockingNamePrefixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBlockingPrefix("prefix").build();
        final var adapted = configurer.blockingNamePrefix(original);
        assertEquals(original.blockingPrefix(), adapted.blockingPrefix());
    }

    @Test
    void blockingNamePrefixChange() {
        final var original = SqlConfiguration.usingDefaults().setBlockingPrefix(" ").build();
        final var adapted = configurer.blockingNamePrefix(original);
        assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix());
    }

    @Test
    void blockingNameSuffixKeep() {
        final var original = SqlConfiguration.usingDefaults().setBlockingSuffix("suffix").build();
        final var adapted = configurer.blockingNameSuffix(original);
        assertEquals(original.blockingSuffix(), adapted.blockingSuffix());
    }

    @Test
    void blockingNameSuffixChange() {
        final var original = SqlConfiguration.usingDefaults().setBlockingSuffix(" ").build();
        final var adapted = configurer.blockingNameSuffix(original);
        assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix());
    }

    @Test
    void affixesKeep() {
        final var original = SqlConfiguration.usingDefaults()
                .setBatchPrefix("prefix")
                .setBatchSuffix("suffix")
                .setBlockingPrefix("prefix")
                .setBlockingSuffix("suffix")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertEquals(original.batchPrefix(), adapted.batchPrefix()),
                () -> assertEquals(original.batchSuffix(), adapted.batchSuffix()),
                () -> assertEquals(original.blockingPrefix(), adapted.blockingPrefix()),
                () -> assertEquals(original.blockingSuffix(), adapted.blockingSuffix()));
    }

    @Test
    void affixesChange() {
        final var original = SqlConfiguration.usingDefaults()
                .setBatchPrefix(" ")
                .setBatchSuffix(" ")
                .setBlockingPrefix(" ")
                .setBlockingSuffix(" ")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertEquals(repositories.batchPrefix(), adapted.batchPrefix()),
                () -> assertEquals(repositories.batchSuffix(), adapted.batchSuffix()),
                () -> assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix()),
                () -> assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix()));
    }

    @Test
    void baseNameKeep() {
        final var original = SqlConfiguration.usingDefaults().setName("name").build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals(original.name(), adapted.name());
    }

    @Test
    void baseNameInvalidNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("readNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "some", 2);
        assertEquals("readNameWasChanged2", adapted.name());
    }

    @Test
    void baseNameInvalidNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("updateNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "some", 3);
        assertEquals("updateNameWasChanged3", adapted.name());
    }

    @Test
    void baseNameInvalidNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("callNameWasChanged", adapted.name());
    }

    @Test
    void baseNameInvalidNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "some", 4);
        assertEquals("callNameWasChanged4", adapted.name());
    }

    @Test
    void baseNameBlankName() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals("some", adapted.name());
    }

    @Test
    void baseNameBlankNameNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertEquals("some", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 1);
        assertEquals("statementNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("readNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertEquals("readNameWasChanged2", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("updateNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertEquals("updateNameWasChanged3", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertEquals("callNameWasChanged", adapted.name());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertEquals("callNameWasChanged3", adapted.name());
    }

}
