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
import wtf.metio.yosql.internals.testing.configs.RepositoriesConfigurations;
import wtf.metio.yosql.models.configuration.SqlStatementType;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

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
    void executeOncePrefixKeep() {
        final var original = SqlConfiguration.builder().setExecuteOncePrefix("prefix").build();
        final var adapted = configurer.executeOncePrefix(original);
        assertEquals(original.executeOncePrefix(), adapted.executeOncePrefix());
    }

    @Test
    void executeOncePrefixChange() {
        final var original = SqlConfiguration.builder().setExecuteOncePrefix(" ").build();
        final var adapted = configurer.executeOncePrefix(original);
        assertTrue(adapted.executeOncePrefix().isPresent());
        assertEquals(repositories.executeOncePrefix(), adapted.executeOncePrefix().get());
    }

    @Test
    void executeOnceSuffixKeep() {
        final var original = SqlConfiguration.builder().setExecuteOnceSuffix("suffix").build();
        final var adapted = configurer.executeOnceSuffix(original);
        assertEquals(original.executeOnceSuffix(), adapted.executeOnceSuffix());
    }

    @Test
    void executeOnceSuffixChange() {
        final var original = SqlConfiguration.builder().setExecuteOnceSuffix(" ").build();
        final var adapted = configurer.executeOnceSuffix(original);
        assertTrue(adapted.executeOnceSuffix().isPresent());
        assertEquals(repositories.executeOnceSuffix(), adapted.executeOnceSuffix().get());
    }

    @Test
    void executeBatchPrefixKeep() {
        final var original = SqlConfiguration.builder().setExecuteBatchPrefix("prefix").build();
        final var adapted = configurer.executeBatchPrefix(original);
        assertEquals(original.executeBatchPrefix(), adapted.executeBatchPrefix());
    }

    @Test
    void executeBatchPrefixChange() {
        final var original = SqlConfiguration.builder().setExecuteBatchPrefix(" ").build();
        final var adapted = configurer.executeBatchPrefix(original);
        assertTrue(adapted.executeBatchPrefix().isPresent());
        assertEquals(repositories.executeBatchPrefix(), adapted.executeBatchPrefix().get());
    }

    @Test
    void executeBatchSuffixKeep() {
        final var original = SqlConfiguration.builder().setExecuteBatchSuffix("suffix").build();
        final var adapted = configurer.executeBatchSuffix(original);
        assertEquals(original.executeBatchSuffix(), adapted.executeBatchSuffix());
    }

    @Test
    void executeBatchSuffixChange() {
        final var original = SqlConfiguration.builder().setExecuteBatchSuffix(" ").build();
        final var adapted = configurer.executeBatchSuffix(original);
        assertTrue(adapted.executeBatchSuffix().isPresent());
        assertEquals(repositories.executeBatchSuffix(), adapted.executeBatchSuffix().get());
    }

    @Test
    void executeManyPrefixKeep() {
        final var original = SqlConfiguration.builder().setExecuteManyPrefix("prefix").build();
        final var adapted = configurer.executeManyPrefix(original);
        assertEquals(original.executeManyPrefix(), adapted.executeManyPrefix());
    }

    @Test
    void executeManyPrefixChange() {
        final var original = SqlConfiguration.builder().setExecuteManyPrefix(" ").build();
        final var adapted = configurer.executeManyPrefix(original);
        assertTrue(adapted.executeManyPrefix().isPresent());
        assertEquals(repositories.executeManyPrefix(), adapted.executeManyPrefix().get());
    }

    @Test
    void executeManySuffixKeep() {
        final var original = SqlConfiguration.builder().setExecuteManySuffix("suffix").build();
        final var adapted = configurer.executeManySuffix(original);
        assertEquals(original.executeManySuffix(), adapted.executeManySuffix());
    }

    @Test
    void executeManySuffixChange() {
        final var original = SqlConfiguration.builder().setExecuteManySuffix(" ").build();
        final var adapted = configurer.executeManySuffix(original);
        assertTrue(adapted.executeManySuffix().isPresent());
        assertEquals(repositories.executeManySuffix(), adapted.executeManySuffix().get());
    }

    @Test
    void affixesKeep() {
        final var original = SqlConfiguration.builder()
                .setExecuteOncePrefix("prefix")
                .setExecuteOnceSuffix("suffix")
                .setExecuteBatchPrefix("prefix")
                .setExecuteBatchSuffix("suffix")
                .setExecuteManyPrefix("prefix")
                .setExecuteManySuffix("suffix")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertEquals(original.executeOncePrefix(), adapted.executeOncePrefix()),
                () -> assertEquals(original.executeOnceSuffix(), adapted.executeOnceSuffix()),
                () -> assertEquals(original.executeBatchPrefix(), adapted.executeBatchPrefix()),
                () -> assertEquals(original.executeBatchSuffix(), adapted.executeBatchSuffix()),
                () -> assertEquals(original.executeManyPrefix(), adapted.executeManyPrefix()),
                () -> assertEquals(original.executeManySuffix(), adapted.executeManySuffix()));
    }

    @Test
    void affixesChange() {
        final var original = SqlConfiguration.builder()
                .setExecuteOncePrefix(" ")
                .setExecuteOnceSuffix(" ")
                .setExecuteBatchPrefix(" ")
                .setExecuteBatchSuffix(" ")
                .setExecuteManyPrefix(" ")
                .setExecuteManySuffix(" ")
                .build();
        final var adapted = configurer.affixes(original);
        assertAll(
                () -> assertTrue(adapted.executeOncePrefix().isPresent()),
                () -> assertTrue(adapted.executeOnceSuffix().isPresent()),
                () -> assertTrue(adapted.executeBatchPrefix().isPresent()),
                () -> assertTrue(adapted.executeBatchSuffix().isPresent()),
                () -> assertTrue(adapted.executeManyPrefix().isPresent()),
                () -> assertTrue(adapted.executeManySuffix().isPresent()));
        assertAll(
                () -> assertEquals(repositories.executeOncePrefix(), adapted.executeOncePrefix().get()),
                () -> assertEquals(repositories.executeOnceSuffix(), adapted.executeOnceSuffix().get()),
                () -> assertEquals(repositories.executeBatchPrefix(), adapted.executeBatchPrefix().get()),
                () -> assertEquals(repositories.executeBatchSuffix(), adapted.executeBatchSuffix().get()),
                () -> assertEquals(repositories.executeManyPrefix(), adapted.executeManyPrefix().get()),
                () -> assertEquals(repositories.executeManySuffix(), adapted.executeManySuffix().get()));
    }

    @Test
    void baseNameKeep() {
        final var original = SqlConfiguration.builder().setName("name").build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals(original.name(), adapted.name());
    }

    @Test
    void baseNameInvalidNameUnknownType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isEmpty());
    }

    @Test
    void baseNameInvalidNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .build();
        final var adapted = configurer.baseName(original, "filename", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isEmpty());
    }

    @Test
    void baseNameInvalidNameReadType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameReadTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename2", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged2", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameWriteType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameWriteTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged3", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameCallType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameCallTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "filename", 4);
        assertTrue(adapted.name().isPresent());
        assertEquals("filename4", adapted.name().get());
    }

    @Test
    void baseNameInvalidNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("!@#$%")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 4);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged4", adapted.name().get());
    }

    @Test
    void baseNameBlankName() {
        final var original = SqlConfiguration.builder()
                .setName("")
                .build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("some", adapted.name().get());
    }

    @Test
    void baseNameBlankNameNumbered() {
        final var original = SqlConfiguration.builder()
                .setName("")
                .build();
        final var adapted = configurer.baseName(original, "some", 1);
        assertTrue(adapted.name().isPresent());
        assertEquals("some", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownType() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isEmpty());
    }

    @Test
    void baseNameBlankNameInvalidFileNameUnknownTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 1);
        assertTrue(adapted.name().isEmpty());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadType() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameReadTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.READING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 2);
        assertTrue(adapted.name().isPresent());
        assertEquals("readNameWasChanged2", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteType() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameWriteTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.WRITING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("updateNameWasChanged3", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallType() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 0);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged", adapted.name().get());
    }

    @Test
    void baseNameBlankNameInvalidFileNameCallTypeNumbered() {
        final var original = SqlConfiguration.builder()
                .setName(" ")
                .setType(SqlStatementType.CALLING)
                .build();
        final var adapted = configurer.baseName(original, "!@#$%", 3);
        assertTrue(adapted.name().isPresent());
        assertEquals("callNameWasChanged3", adapted.name().get());
    }

}
