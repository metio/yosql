/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.internals.model.generator.cli.CliGenerator;
import wtf.metio.yosql.internals.model.generator.gradle.GradleGenerator;
import wtf.metio.yosql.internals.model.generator.immutables.ImmutablesGenerator;
import wtf.metio.yosql.internals.model.generator.maven.MavenGenerator;
import wtf.metio.yosql.internals.model.generator.website.MarkdownGenerator;
import wtf.metio.yosql.internals.model.generator.website.RawTextMustacheFactory;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.data.AllConfigurations;
import wtf.metio.yosql.models.meta.data.Runtime;
import wtf.metio.yosql.models.meta.data.Sql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ModelGenerator {

    private final Path outputDirectory;
    private final String immutablesBasePackage;
    private final String basePackage;

    public ModelGenerator(
            final Path outputDirectory,
            final String immutablesBasePackage,
            final String basePackage) {
        this.outputDirectory = outputDirectory;
        this.immutablesBasePackage = immutablesBasePackage;
        this.basePackage = basePackage;
    }

    public void createImmutableModel() {
        final var generator = new ImmutablesGenerator(immutablesBasePackage);
        final var writer = typeWriter(immutablesBasePackage);
        forAllConfigurations(generator, writer);
        writer.accept(generator.apply(Runtime.configurationGroup(immutablesBasePackage)));
        writer.accept(generator.apply(Sql.configurationGroup()));
    }

    public void createMavenModel() {
        final var generator = new MavenGenerator(immutablesBasePackage);
        final var writer = typeWriter(basePackage);
        forAllConfigurations(generator, writer);
    }

    public void createCliModel() {
        final var generator = new CliGenerator(immutablesBasePackage);
        final var writer = typeWriter(basePackage);
        forAllConfigurations(generator, writer);
    }

    public void createGradleModel() {
        final var generator = new GradleGenerator(immutablesBasePackage);
        final var writer = typeWriter(basePackage);
        forAllConfigurations(generator, writer);
    }

    public void createMarkdownDocumentation(final String version) {
        final var factory = new RawTextMustacheFactory();
        final var generator = new MarkdownGenerator(factory, version);
        AllConfigurations.allConfigurationGroups().forEach(group ->
                writeMarkdownFiles(generator, group));
    }

    private void writeMarkdownFiles(final MarkdownGenerator generator, final ConfigurationGroup group) {
        try {
            final var groupDirectory = outputDirectory.resolve(group.name().toLowerCase(Locale.ROOT));
            Files.createDirectories(groupDirectory);
            writeString(groupDirectory.resolve("_index.md"), generator.group(group));
            group.settings().forEach(setting ->
                    writeString(groupDirectory.resolve(setting.name() + ".md"), generator.setting(group, setting)));
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void writeString(final Path path, final CharSequence content) {
        try {
            Files.writeString(path, content, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static void forAllConfigurations(final Function<ConfigurationGroup, TypeSpec> generator, final Consumer<TypeSpec> writer) {
        AllConfigurations.allConfigurationGroups()
                .stream().map(generator).forEach(writer);
    }

    private Consumer<TypeSpec> typeWriter(final String targetPackageName) {
        return typeSpec -> {
            try {
                JavaFile.builder(targetPackageName, typeSpec)
                        .build()
                        .writeTo(outputDirectory);
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
