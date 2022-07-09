/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.codegen.dao;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wtf.metio.yosql.codegen.blocks.BlocksObjectMother;
import wtf.metio.yosql.internals.testing.configs.JavaConfigurations;
import wtf.metio.yosql.internals.testing.configs.NamesConfigurations;
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
        generator = new DefaultParameterGenerator(
                BlocksObjectMother.parameters(JavaConfigurations.defaults()),
                NamesConfigurations.defaults());
    }

    @Test
    void asParameterSpecs() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name, "first name"),
                () -> assertEquals(TypeName.OBJECT, parameterSpecs.get(0).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name, "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(1).type, "second type"));
    }

    @Test
    void asParameterSpecsWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name, "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type, "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name, "first name"),
                () -> assertEquals(TypeName.OBJECT, parameterSpecs.get(1).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name, "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(2).type, "second type"));
    }

    @Test
    void asParameterSpecsForInterfaces() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name, "first name"),
                () -> assertEquals(TypeName.OBJECT, parameterSpecs.get(0).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name, "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(1).type, "second type"));
    }

    @Test
    void asParameterSpecsForInterfacesWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name, "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type, "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name, "first name"),
                () -> assertEquals(TypeName.OBJECT, parameterSpecs.get(1).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name, "second name"),
                () -> assertEquals(TypeName.INT, parameterSpecs.get(2).type, "second type"));
    }

    @Test
    void asBatchParameterSpecs() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asBatchParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name, "first name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.OBJECT), parameterSpecs.get(0).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name, "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(1).type, "second type"));
    }

    @Test
    void asBatchParameterSpecsWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asBatchParameterSpecs(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name, "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type, "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name, "first name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.OBJECT), parameterSpecs.get(1).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name, "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(2).type, "second type"));
    }

    @Test
    void asBatchParameterSpecsForInterfaces() {
        final var configuration = SqlConfigurations.sqlConfiguration();
        final var parameterSpecs = generator.asBatchParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(2, parameterSpecs.size()),
                () -> assertEquals("test", parameterSpecs.get(0).name, "first name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.OBJECT), parameterSpecs.get(0).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(1).name, "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(1).type, "second type"));
    }

    @Test
    void asBatchParameterSpecsForInterfacesWithGivenConnection() {
        final var configuration = SqlConfiguration.copyOf(SqlConfigurations.sqlConfiguration())
                .withCreateConnection(false);
        final var parameterSpecs = generator.asBatchParameterSpecsForInterfaces(configuration);
        assertAll(
                () -> assertEquals(3, parameterSpecs.size()),
                () -> assertEquals("connection", parameterSpecs.get(0).name, "connection name"),
                () -> assertEquals(ClassName.get(Connection.class), parameterSpecs.get(0).type, "connection type"),
                () -> assertEquals("test", parameterSpecs.get(1).name, "first name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.OBJECT), parameterSpecs.get(1).type, "first type"),
                () -> assertEquals("id", parameterSpecs.get(2).name, "second name"),
                () -> assertEquals(ArrayTypeName.of(TypeName.INT), parameterSpecs.get(2).type, "second type"));
    }

}
