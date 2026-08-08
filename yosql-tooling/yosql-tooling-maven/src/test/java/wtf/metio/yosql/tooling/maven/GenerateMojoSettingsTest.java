/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.maven;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.immutables.SchemaConfiguration;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("an incremental build asks what the settings decide, not only what the files say")
class GenerateMojoSettingsTest {

    private static RuntimeConfiguration configuration() {
        return RuntimeConfiguration.builder().build();
    }

    private static RuntimeConfiguration into(final String basePackage) {
        return RuntimeConfiguration.copyOf(configuration())
                .withRepositories(RepositoriesConfiguration.builder()
                        .setBasePackageName(basePackage)
                        .build());
    }

    @Test
    @DisplayName("with nothing remembered there is nothing to compare, so it generates")
    void shouldGenerateWithoutAPreviousRun(@TempDir final Path directory) {
        assertTrue(GenerateMojo.settingsChanged(directory.resolve("absent.txt"), configuration()));
    }

    @Test
    @DisplayName("settings that decided the code on disk do not decide it again")
    void shouldNotRegenerateForUnchangedSettings(@TempDir final Path directory) {
        final var settings = directory.resolve("yosql").resolve("settings.txt");
        GenerateMojo.recordSettings(settings, configuration());

        assertFalse(GenerateMojo.settingsChanged(settings, configuration()));
    }

    /**
     * The reason this exists: nothing under a watched directory changes when a setting does, so the
     * delta check finds none and the output of the old settings stays on disk and compiles — the
     * edit appears to have done nothing.
     */
    @Test
    @DisplayName("a setting changed and no file touched still generates")
    void shouldRegenerateForChangedSettings(@TempDir final Path directory) {
        final var settings = directory.resolve("yosql").resolve("settings.txt");
        GenerateMojo.recordSettings(settings, into("com.example.first"));

        assertAll(
                () -> assertTrue(GenerateMojo.settingsChanged(settings, into("com.example.second"))),
                () -> assertFalse(GenerateMojo.settingsChanged(settings, into("com.example.first"))));
    }

    @Test
    @DisplayName("a setting of any group counts, not only the ones naming directories")
    void shouldNoticeASchemaSetting(@TempDir final Path directory) {
        final var settings = directory.resolve("yosql").resolve("settings.txt");
        GenerateMojo.recordSettings(settings, configuration());

        final var withVendor = RuntimeConfiguration.copyOf(configuration())
                .withSchema(SchemaConfiguration.builder().setVendor("PostgreSQL").build());

        assertTrue(GenerateMojo.settingsChanged(settings, withVendor),
                "the vendor decides which type spellings the schema is read in");
    }

    @Test
    @DisplayName("the same settings read the same way twice, so a quiet build stays quiet")
    void shouldBeStableAcrossRuns(@TempDir final Path directory) {
        final var settings = directory.resolve("yosql").resolve("settings.txt");
        GenerateMojo.recordSettings(settings, into("com.example.persistence"));

        // Built again from scratch rather than reused: an unstable rendering would regenerate on
        // every build, which is only wasteful rather than wrong, and would go unnoticed for exactly
        // that reason.
        assertAll(
                () -> assertFalse(GenerateMojo.settingsChanged(settings, into("com.example.persistence"))),
                () -> assertFalse(GenerateMojo.settingsChanged(settings, into("com.example.persistence"))),
                () -> assertFalse(GenerateMojo.settingsChanged(settings, into("com.example.persistence"))));
    }

    @Test
    @DisplayName("what it writes can be read back as what it wrote")
    void shouldRecordSomethingLegible(@TempDir final Path directory) {
        final var settings = directory.resolve("deep").resolve("nested").resolve("settings.txt");
        GenerateMojo.recordSettings(settings, into("com.example.persistence"));

        assertAll(
                () -> assertTrue(java.nio.file.Files.exists(settings), "the parent directories are made"),
                () -> assertTrue(java.nio.file.Files.readString(settings).contains("com.example.persistence"),
                        "a maintainer can diff two of these and read why a build regenerated"));
    }

}
