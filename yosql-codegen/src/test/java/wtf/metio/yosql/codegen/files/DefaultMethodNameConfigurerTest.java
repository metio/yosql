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

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(adapted.batchPrefix().isPresent());
        assertEquals(repositories.batchPrefix(), adapted.batchPrefix().get());
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
        assertTrue(adapted.batchSuffix().isPresent());
        assertEquals(repositories.batchSuffix(), adapted.batchSuffix().get());
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
        assertTrue(adapted.blockingPrefix().isPresent());
        assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix().get());
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
        assertTrue(adapted.blockingSuffix().isPresent());
        assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix().get());
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
                () -> assertTrue(adapted.batchPrefix().isPresent()),
                () -> assertTrue(adapted.batchSuffix().isPresent()),
                () -> assertTrue(adapted.blockingPrefix().isPresent()),
                () -> assertTrue(adapted.blockingSuffix().isPresent()));
        assertAll(
                () -> assertEquals(repositories.batchPrefix(), adapted.batchPrefix().get()),
                () -> assertEquals(repositories.batchSuffix(), adapted.batchSuffix().get()),
                () -> assertEquals(repositories.blockingPrefix(), adapted.blockingPrefix().get()),
                () -> assertEquals(repositories.blockingSuffix(), adapted.blockingSuffix().get()));
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
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("statementNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "filename", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("statementNameWasChanged3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename2", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged2", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 4);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename4", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("!@#$%")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 4);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged4", adapted.name().get());
    }

    @Test
    void baseNameBlankName() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("some", adapted.name().get());
    }

    @Test
    void baseNameBlankNameNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName("")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertTrue(adapted.name().isPresent());
        assertEquals("some", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("statementNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.UNKNOWN)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 1);
        assertTrue(adapted.name().isPresent());
        assertEquals("statementNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged2", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged3", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.usingDefaults()
                .setName(" ")
                .setType(SqlType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged3", adapted.name().get());
    }

}
