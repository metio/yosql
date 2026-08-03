/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.models.meta.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import wtf.metio.yosql.models.meta.ConfigurationGroup;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Every configuration group has to be reachable from every frontend.
 *
 * <p>The meta-model emits a class per group per frontend, but what declares it — a mojo parameter, an
 * Ant element, a picocli mixin, a Gradle extension — is written by hand, once per frontend. Adding a
 * group to the meta-model therefore compiles, generates, documents itself and reaches nobody, and the
 * examples cannot notice because they configure only what they need. The schema group shipped that
 * way: the website told people to write a {@code <schema>} block that no build would have accepted.</p>
 *
 * <p>Reading source rather than calling anything is deliberate. Two of these frontends are not on this
 * module's classpath, and one of them — Gradle — is not built by Maven at all.</p>
 */
@DisplayName("every frontend can configure every group")
class FrontendCoverageTest {

    /**
     * Where each frontend turns its own configuration objects into a {@code RuntimeConfiguration}.
     * A group is reachable when it is passed here, which is stronger than a field existing: a
     * declared-but-unused mojo parameter would still leave the setting doing nothing.
     */
    private static final List<Path> WIRING = List.of(
            Path.of("yosql-tooling", "yosql-tooling-maven", "src", "main", "java", "wtf", "metio",
                    "yosql", "tooling", "maven", "GenerateMojo.java"),
            Path.of("yosql-tooling", "yosql-tooling-ant", "src", "main", "java", "wtf", "metio",
                    "yosql", "tooling", "ant", "YoSQLGenerateTask.java"),
            Path.of("yosql-tooling", "yosql-tooling-cli", "src", "main", "java", "wtf", "metio",
                    "yosql", "tooling", "cli", "Generate.java"),
            Path.of("yosql-tooling", "yosql-tooling-gradle", "src", "main", "java", "wtf", "metio",
                    "yosql", "tooling", "gradle", "GenerateTaskConfiguration.java"));

    static Stream<Object[]> everyGroupInEveryFrontend() {
        return AllConfigurations.allConfigurationGroups()
                .map(ConfigurationGroup::name)
                .flatMap(group -> WIRING.stream().map(frontend -> new Object[]{group, frontend}));
    }

    @ParameterizedTest(name = "{0} reaches {1}")
    @MethodSource("everyGroupInEveryFrontend")
    void shouldBeWiredIntoEveryFrontend(final String group, final Path frontend) {
        final var source = read(repositoryRoot().resolve(frontend));
        final var setter = ".set" + group + "(";

        if (!source.contains(setter)) {
            throw new AssertionError(("%s does not pass the %s configuration on: no '%s' in it. A group "
                    + "the meta-model knows about but a frontend never reads is a setting the "
                    + "documentation promises and the build rejects.")
                    .formatted(frontend.getFileName(), group, setter));
        }
    }

    /**
     * This module sits two directories below the root, and the frontends are read relative to it.
     */
    private static Path repositoryRoot() {
        return Path.of("").toAbsolutePath().getParent().getParent();
    }

    private static String read(final Path source) {
        try {
            return Files.readString(source);
        } catch (final IOException cause) {
            throw new UncheckedIOException(
                    "Cannot read %s. A frontend moved, and this test is how that gets noticed."
                            .formatted(source), cause);
        }
    }

}
