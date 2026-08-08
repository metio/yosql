/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.tooling.maven;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Wherever the documentation shows how to declare this plugin, it has to show the coordinate the
 * plugin is actually published under.
 *
 * <p>This module inherits its group from {@code yosql-tooling/pom.xml}, which is not the group the
 * reactor root carries, so a coordinate written out by hand is a guess about a file two directories
 * away. Every page said {@code wtf.metio.yosql}, which resolves to nothing on Central: the first
 * thing a new user copied out of the tutorial failed to build, and nothing here noticed, because the
 * examples that do build never spell the group out — they inherit it.</p>
 *
 * <p>The group comes from the example rather than from a POM of ours because the example is the one
 * declaration CI proves: {@code yosql-examples-maven} names the plugin in full and generates code
 * with it on every build.</p>
 */
@DisplayName("the documentation names the coordinate this plugin is published under")
class DocumentedCoordinateTest {

    private static final String ARTIFACT = "yosql-tooling-maven";

    /**
     * The declaration CI runs, and therefore the answer.
     */
    private static final Path WORKING_EXAMPLE =
            Path.of("yosql-examples", "yosql-examples-maven", "pom.xml");

    /**
     * A {@code <groupId>} holding the plugin's own group is one that sits directly in front of its
     * {@code <artifactId>}. Anything else in these files belongs to some other artifact.
     */
    private static final Pattern DECLARED = Pattern.compile(
            "<groupId>\\s*([\\w.]+)\\s*</groupId>\\s*<artifactId>\\s*" + ARTIFACT + "\\s*</artifactId>");

    private static final Pattern XML_TAG = Pattern.compile("</?(?:div|pre|code|span)\\b[^>]*>");

    /**
     * Prose that shows the coordinate. The reference pages under {@code docs/content/configuration}
     * are left out on purpose: they are generated from {@code configurationSetting.md} and gitignored,
     * so the template is where a wrong group would have to be fixed, and a stale checkout of the
     * output would fail this for a reason nobody can act on.
     */
    static Stream<Path> documentation() {
        return Stream.of(
                Path.of("yosql-models", "yosql-models-generator", "src", "main", "resources",
                        "configurationSetting.md"),
                Path.of("docs", "layouts", "shortcodes", "maven", "tooling", "index.html"),
                Path.of("docs", "layouts", "shortcodes", "maven", "tooling", "full.html"),
                Path.of("docs", "layouts", "shortcodes", "maven", "tooling", "multi.html"),
                Path.of("docs", "content", "installation", "tutorial.md"),
                Path.of("docs", "content", "tooling", "maven.md"),
                Path.of("docs", "content", "frameworks", "spring-boot.md"),
                Path.of("docs", "content", "frameworks", "quarkus.md"));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("documentation")
    void shouldShowThePublishedGroup(final Path page) {
        final var text = readable(repositoryRoot().resolve(page));
        final var published = publishedGroup();

        var mentions = 0;
        final var matcher = DECLARED.matcher(text);
        while (matcher.find()) {
            mentions++;
            if (!published.equals(matcher.group(1))) {
                throw new AssertionError(("%s declares %s under '%s', but it is published under '%s'. "
                        + "A build copied out of this page cannot resolve the plugin.")
                        .formatted(page.getFileName(), ARTIFACT, matcher.group(1), published));
            }
        }
        // A page that shows the artifact and no group in front of it has been reworded into
        // something this cannot read, which is the same as being unguarded.
        final var shown = occurrences(text, "<artifactId>" + ARTIFACT + "</artifactId>");
        if (shown != mentions) {
            throw new AssertionError(("%s mentions %s %d time(s) but only %d of them follow a "
                    + "<groupId>. Either the coordinate lost its group or this test can no longer "
                    + "read the page.").formatted(page.getFileName(), ARTIFACT, shown, mentions));
        }

        // Links to Central carry the group as a path segment, and go nowhere when it is wrong.
        final var links = Pattern.compile("(?:artifact|namespace)/([\\w.]+)/" + ARTIFACT).matcher(text);
        while (links.find()) {
            if (!published.equals(links.group(1))) {
                throw new AssertionError(("%s links to %s under '%s', but it is published under '%s'.")
                        .formatted(page.getFileName(), ARTIFACT, links.group(1), published));
            }
        }
    }

    private static String publishedGroup() {
        final var matcher = DECLARED.matcher(readable(repositoryRoot().resolve(WORKING_EXAMPLE)));
        if (!matcher.find()) {
            throw new AssertionError(("%s no longer declares %s in full. It is where the published "
                    + "group is read from, because it is the declaration the build proves.")
                    .formatted(WORKING_EXAMPLE, ARTIFACT));
        }
        return matcher.group(1);
    }

    /**
     * The shortcodes are pre-highlighted XML: the markup is chroma's, and the coordinate underneath
     * it is escaped. Dropping the one and unescaping the other leaves the XML a reader copies.
     */
    private static String readable(final Path source) {
        var text = read(source);
        if (source.getFileName().toString().endsWith(".html")) {
            text = XML_TAG.matcher(text).replaceAll("")
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&amp;", "&");
        }
        return text;
    }

    private static int occurrences(final String text, final String needle) {
        var count = 0;
        for (var index = text.indexOf(needle); index >= 0; index = text.indexOf(needle, index + 1)) {
            count++;
        }
        return count;
    }

    /**
     * This module sits two directories below the root, and the documentation is read relative to it.
     */
    private static Path repositoryRoot() {
        return Path.of("").toAbsolutePath().getParent().getParent();
    }

    private static String read(final Path source) {
        try {
            return Files.readString(source);
        } catch (final IOException cause) {
            throw new UncheckedIOException(
                    "Cannot read %s. A page moved, and this test is how that gets noticed."
                            .formatted(source), cause);
        }
    }

}
