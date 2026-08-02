/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.cli;

import picocli.CommandLine;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Writes the smallest project that generates something, so that the first run is a run rather than
 * a reading exercise.
 *
 * <p>What it writes is what the documentation's first example describes: one statement, the record
 * it builds, and the arguments file that ties the two directories together. Running
 * {@code yosql generate @yosql.args} straight afterwards produces a repository.</p>
 */
@CommandLine.Command(
        name = "init",
        description = "Write a statement, a record and an arguments file to start from",
        versionProvider = VersionProvider.class,
        mixinStandardHelpOptions = true,
        usageHelpAutoWidth = true,
        showDefaultValues = true
)
public class Init implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-d", "--directory"},
            description = "The project directory to write into.")
    Path directory = Path.of(System.getProperty("user.dir"));

    @CommandLine.Option(
            names = {"-p", "--package"},
            description = "The package repositories and records are written in.")
    String basePackage = "com.example";

    @CommandLine.Option(
            names = "--sql-directory",
            description = "Where statements live, relative to the project directory.")
    String sqlDirectory = "src/main/yosql";

    @CommandLine.Option(
            names = "--source-directory",
            description = "Where Java sources live, relative to the project directory.")
    String sourceDirectory = "src/main/java";

    @CommandLine.Option(
            names = "--force",
            description = "Overwrite files that are already there.")
    boolean force;

    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public Integer call() throws IOException {
        final var out = spec.commandLine().getOut();
        final var domainPackage = basePackage + ".domain";
        final var persistencePackage = basePackage + ".persistence";
        final var domainDirectory = directory.resolve(sourceDirectory)
                .resolve(domainPackage.replace('.', '/'));

        final var written = new ArrayList<Path>();
        final var skipped = new ArrayList<Path>();

        write(directory.resolve(sqlDirectory).resolve("tenant").resolve("findTenant.sql"),
                statement(domainPackage), written, skipped);
        write(domainDirectory.resolve("Tenant.java"),
                record(domainPackage), written, skipped);
        write(directory.resolve("yosql.args"),
                arguments(persistencePackage), written, skipped);

        report(out, written, skipped);
        return CommandLine.ExitCode.OK;
    }

    private void write(
            final Path target,
            final String content,
            final List<Path> written,
            final List<Path> skipped) throws IOException {
        if (Files.exists(target) && !force) {
            skipped.add(target);
            return;
        }
        Files.createDirectories(target.getParent());
        Files.writeString(target, content, StandardCharsets.UTF_8);
        written.add(target);
    }

    private void report(final PrintWriter out, final List<Path> written, final List<Path> skipped) {
        written.forEach(path -> out.println("wrote " + directory.relativize(path)));
        skipped.forEach(path -> out.println("kept  " + directory.relativize(path)
                + " (already there — pass --force to overwrite)"));
        if (!written.isEmpty()) {
            out.println();
            out.println("Next: yosql generate @yosql.args");
        }
    }

    private static String statement(final String domainPackage) {
        return """
                -- name: findTenant
                -- returning: single
                -- resultRowType: %s.Tenant
                select id, slug, created_at
                from tenant
                where id = :id
                ;

                -- name: insertTenant
                -- returning: none
                -- parameters:
                --   id: uuid
                --   slug: string
                --   createdAt: instant
                insert into tenant (id, slug, created_at)
                values (:id, :slug, :createdAt)
                ;
                """.formatted(domainPackage);
    }

    private static String record(final String domainPackage) {
        return """
                package %s;

                import java.time.Instant;
                import java.util.UUID;

                public record Tenant(UUID id, String slug, Instant createdAt) {
                }
                """.formatted(domainPackage);
    }

    private String arguments(final String persistencePackage) {
        return """
                --files-input-base-directory=%s
                --files-output-base-directory=%s
                --files-source-directory=%s
                --repositories-base-package-name=%s
                """.formatted(sqlDirectory, sourceDirectory, sourceDirectory, persistencePackage);
    }

}
