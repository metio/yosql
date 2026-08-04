/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The reflection metadata has to name exactly the model classes that exist.
 *
 * <p>A closed-world image keeps what is reached by call and drops what is reached by name, so the
 * configuration model Jackson binds is listed here by hand. Both directions of drift are silent:
 * a class deleted from the model leaves an entry pointing at nothing, and a class added to it is
 * simply absent — and absent is the one that matters, because the only symptom is a native binary
 * that cannot read what it was asked to. Neither shows up in any JVM build.</p>
 *
 * <p>Which is what happened: the schema configuration shipped without ever being registered, and
 * eleven entries survived the groups they belonged to. This compares the file against the classes
 * on the classpath, so the next one fails here rather than in a binary somebody has already
 * shipped.</p>
 */
@DisplayName("native image reflection metadata")
class ReflectionMetadataTest {

    private static final String CONFIG = "/META-INF/native-image/wtf.metio.yosql.tooling/"
            + "yosql-tooling-cli/reflect-config.json";

    /**
     * Jackson binds the configuration model, and nothing else of ours is reached by name.
     */
    private static final List<String> REGISTERED_PACKAGES = List.of(
            "wtf/metio/yosql/models/configuration/",
            "wtf/metio/yosql/models/immutables/");

    @Test
    @DisplayName("names every model class, and no class that stopped existing")
    void shouldMatchTheModelOnTheClasspath() {
        assertEquals(compiledModelClasses(), registeredClasses(),
                "reflect-config.json has drifted from the model. Re-derive it from the compiled "
                        + "classes of yosql-models-configuration and yosql-models-immutables: an entry "
                        + "too many is dead weight, one too few is a native binary that fails to read "
                        + "its own configuration.");
    }

    private static Set<String> registeredClasses() {
        try (final var source = ReflectionMetadataTest.class.getResourceAsStream(CONFIG)) {
            final var entries = new ObjectMapper().readTree(source);
            final var names = new TreeSet<String>();
            entries.forEach(entry -> names.add(entry.get("name").asText()));
            return names;
        } catch (final IOException exception) {
            throw new UncheckedIOException("Cannot read " + CONFIG, exception);
        }
    }

    /**
     * Reads the classpath rather than scanning with a library: the model modules arrive as
     * directories inside the reactor and as jars outside it, and both have to answer the same.
     */
    private static Set<String> compiledModelClasses() {
        final var found = new TreeSet<String>();
        for (final var entry : System.getProperty("java.class.path").split(java.io.File.pathSeparator)) {
            final var path = Path.of(entry);
            if (Files.isDirectory(path)) {
                collectFromDirectory(path, found);
            } else if (entry.endsWith(".jar")) {
                collectFromJar(path, found);
            }
        }
        return found;
    }

    private static void collectFromDirectory(final Path root, final Set<String> found) {
        try (final var files = Files.walk(root)) {
            files.filter(Files::isRegularFile)
                    .map(file -> root.relativize(file).toString().replace(java.io.File.separatorChar, '/'))
                    .forEach(name -> addWhenModelClass(name, found));
        } catch (final IOException exception) {
            throw new UncheckedIOException("Cannot read " + root, exception);
        }
    }

    private static void collectFromJar(final Path jar, final Set<String> found) {
        try (final var archive = new ZipFile(jar.toFile())) {
            archive.stream().map(java.util.zip.ZipEntry::getName).forEach(name -> addWhenModelClass(name, found));
        } catch (final IOException _) {
            // A classpath entry that is not a readable archive says nothing about the model.
        }
    }

    private static void addWhenModelClass(final String name, final Set<String> found) {
        if (!name.endsWith(".class") || name.endsWith("package-info.class")) {
            return;
        }
        if (REGISTERED_PACKAGES.stream().noneMatch(name::startsWith)) {
            return;
        }
        found.add(name.substring(0, name.length() - ".class".length()).replace('/', '.'));
    }

}
