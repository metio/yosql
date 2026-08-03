/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.internals.testing.configs.SqlConfigurations;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DefaultParameterGenerator")
class DefaultParameterGeneratorTest {

    private DefaultParameterGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new DefaultParameterGenerator(BlocksObjectMother.parameters());
    }

    @Test
    void asParameterSpecs() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name(), "first name"),
                () -> assertEquals(ClassName.OBJECT, parameterSpecs.get(0).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name(), "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(1).type(), "second type"));
    }

    @Test
    void asParameterSpecsWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name(), "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type(), "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name(), "first name"),
                () -> assertEquals(ClassName.OBJECT, parameterSpecs.get(1).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name(), "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(2).type(), "second type"));
    }

    @Test
    void asParameterSpecsForInterfaces() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name(), "first name"),
                () -> assertEquals(ClassName.OBJECT, parameterSpecs.get(0).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name(), "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(1).type(), "second type"));
    }

    @Test
    void asParameterSpecsForInterfacesWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name(), "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type(), "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name(), "first name"),
                () -> assertEquals(ClassName.OBJECT, parameterSpecs.get(1).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name(), "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(2).type(), "second type"));
    }

    @Test
    void asBatchParameterSpecs() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asBatchParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name(), "first name"),
                () -> assertEquals(ArrayTypeName.of(ClassName.OBJECT), parameterSpecs.get(0).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name(), "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(1).type(), "second type"));
    }

    @Test
    void asBatchParameterSpecsWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asBatchParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name(), "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type(), "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name(), "first name"),
                () -> assertEquals(ArrayTypeName.of(ClassName.OBJECT), parameterSpecs.get(1).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name(), "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(2).type(), "second type"));
    }

    @Test
    void asBatchParameterSpecsForInterfaces() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asBatchParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name(), "first name"),
                () -> assertEquals(ArrayTypeName.of(ClassName.OBJECT), parameterSpecs.get(0).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name(), "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(1).type(), "second type"));
    }

    @Test
    void asBatchParameterSpecsForInterfacesWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asBatchParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name(), "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type(), "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name(), "first name"),
                () -> assertEquals(ArrayTypeName.of(ClassName.OBJECT), parameterSpecs.get(1).type(), "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name(), "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(2).type(), "second type"));
    }

}
