/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.cli;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("yosql init")
class InitTest {

    @TempDir
    Path project;

    private final StringWriter output = new StringWriter();

    private int run(final String... arguments) {
        final var commandLine = YoSQL.commandLine();
        commandLine.setOut(new PrintWriter(output));
        return commandLine.execute(concat(arguments));
    }

    private String[] concat(final String... arguments) {
        final var all = new String[arguments.length + 3];
        all[0] = "init";
        all[1] = "--directory";
        all[2] = project.toString();
        System.arraycopy(arguments, 0, all, 3, arguments.length);
        return all;
    }

    @Test
    @DisplayName("writes a statement, the record it builds and an arguments file")
    void shouldWriteAStartingPoint() throws IOException {
        final var exitCode = run();

        assertAll(
                () -> assertEquals(0, exitCode),
                () -> assertTrue(Files.isRegularFile(project.resolve("src/main/yosql/tenant/findTenant.sql"))),
                () -> assertTrue(Files.isRegularFile(project.resolve("src/main/java/com/example/domain/Tenant.java"))),
                () -> assertTrue(Files.isRegularFile(project.resolve("yosql.args"))));
    }

    @Test
    @DisplayName("the statement names the record that is written next to it")
    void shouldPointTheStatementAtTheRecord() throws IOException {
        run();

        final var statement = Files.readString(project.resolve("src/main/yosql/tenant/findTenant.sql"));
        final var record = Files.readString(project.resolve("src/main/java/com/example/domain/Tenant.java"));

        assertAll(
                () -> assertTrue(statement.contains("resultRowType: com.example.domain.Tenant")),
                () -> assertTrue(record.contains("package com.example.domain;")),
                () -> assertTrue(record.contains("public record Tenant(")));
    }

    @Test
    @DisplayName("the arguments file points at the directories that were written")
    void shouldWriteMatchingArguments() throws IOException {
        run();

        final var arguments = Files.readString(project.resolve("yosql.args"));

        assertAll(
                () -> assertTrue(arguments.contains("--files-input-base-directory=src/main/yosql")),
                () -> assertTrue(arguments.contains("--files-source-directory=src/main/java")),
                () -> assertTrue(arguments.contains("--repositories-base-package-name=com.example.persistence")));
    }

    @Test
    @DisplayName("a package of your own reaches the statement, the record and the arguments")
    void shouldHonourTheGivenPackage() throws IOException {
        run("--package", "wtf.metio.shop");

        final var record = project.resolve("src/main/java/wtf/metio/shop/domain/Tenant.java");

        assertAll(
                () -> assertTrue(Files.isRegularFile(record)),
                () -> assertTrue(Files.readString(record).contains("package wtf.metio.shop.domain;")),
                () -> assertTrue(Files.readString(project.resolve("src/main/yosql/tenant/findTenant.sql"))
                        .contains("wtf.metio.shop.domain.Tenant")),
                () -> assertTrue(Files.readString(project.resolve("yosql.args"))
                        .contains("--repositories-base-package-name=wtf.metio.shop.persistence")));
    }

    @Test
    @DisplayName("a file that is already there is left alone")
    void shouldNotOverwrite() throws IOException {
        final var arguments = project.resolve("yosql.args");
        Files.writeString(arguments, "--files-input-base-directory=somewhere/else\n");

        final var exitCode = run();

        assertAll(
                () -> assertEquals(0, exitCode),
                () -> assertEquals("--files-input-base-directory=somewhere/else\n", Files.readString(arguments)),
                () -> assertTrue(output.toString().contains("--force")));
    }

    @Test
    @DisplayName("--force overwrites it")
    void shouldOverwriteWithForce() throws IOException {
        final var arguments = project.resolve("yosql.args");
        Files.writeString(arguments, "--files-input-base-directory=somewhere/else\n");

        run("--force");

        assertFalse(Files.readString(arguments).contains("somewhere/else"));
    }

}
