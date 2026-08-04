/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
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
    void baseNameKeep() {
        final var original = SqlConfiguration.builder().setName("name").build();
        final var adapted = configurer.baseName(original, "some", 0);
        assertEquals(original.name(), adapted.name());
    }

    @Test
    @DisplayName("a qualified name is not a method name, however valid a type name it would be")
    void baseNameRejectsQualifiedName() {
        final var original = SqlConfiguration.builder().setName("com.example.findTenant").build();
        final var adapted = configurer.baseName(original, "filename", 0);
        assertEquals("filename", adapted.name().orElseThrow());
    }

    @Test
    @DisplayName("a file name carrying a dot does not become a method name with a dot in it")
    void baseNameRejectsQualifiedFileName() {
        final var original = SqlConfiguration.builder().build();
        final var adapted = configurer.baseName(original, "findTenant.v2", 0);
        assertTrue(adapted.name().isEmpty());
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
