/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import com.squareup.javapoet.TypeSpec;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.ConfigurationSetting;
import wtf.metio.yosql.models.meta.data.AllConfigurations;
import wtf.metio.yosql.models.meta.data.Runtime;
import wtf.metio.yosql.models.meta.data.Sql;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static wtf.metio.yosql.models.meta.data.ToolingPackages.*;

/**
 * Generator for Java models that delegates most of its work to tooling-specific generators.
 */
public final class ModelGenerator {

    public void createImmutableModel(final BiConsumer<String, TypeSpec> writer) {
        final var generator = new ImmutablesModelGenerator();
        forAllConfigurations(generator, type -> writer.accept(IMMUTABLES_PACKAGE, type));
        generator.apply(Runtime.configurationGroup())
                .forEach(type -> writer.accept(IMMUTABLES_PACKAGE, type));
        generator.apply(Sql.configurationGroup())
                .forEach(type -> writer.accept(IMMUTABLES_PACKAGE, type));
    }

    public void createMavenModel(final BiConsumer<String, TypeSpec> writer) {
        final var generator = new MavenModelGenerator();
        forAllConfigurations(generator, type -> writer.accept(MAVEN_PACKAGE, type));
    }

    public void createCliModel(final BiConsumer<String, TypeSpec> writer) {
        final var generator = new CliModelGenerator();
        forAllConfigurations(generator, type -> writer.accept(CLI_PACKAGE, type));
    }

    public void createAntModel(final BiConsumer<String, TypeSpec> writer) {
        final var generator = new AntModelGenerator();
        forAllConfigurations(generator, type -> writer.accept(ANT_PACKAGE, type));
    }

    public void createGradleModel(final BiConsumer<String, TypeSpec> writer) {
        final var generator = new GradleModelGenerator();
        forAllConfigurations(generator, type -> writer.accept(GRADLE_PACKAGE, type));
    }

    public static void createMarkdownDocumentation(final String version, final Path outputDirectory) {
        final var factory = new RawTextMustacheFactory();
        final var globalGenerator = new MarkdownGenerator(factory, version, "configurationSetting.md");
        final var statementGenerator = new MarkdownGenerator(factory, version, "frontmatterSetting.md");
        AllConfigurations.allConfigurationGroups().forEach(group ->
                writeMarkdownFiles(globalGenerator, group, outputDirectory));
        writeMarkdownFiles(statementGenerator, Sql.configurationGroup(), outputDirectory);
    }

    private static void writeMarkdownFiles(final MarkdownGenerator generator, final ConfigurationGroup group, final Path outputDirectory) {
        try {
            final var groupDirectory = outputDirectory.resolve(group.name().toLowerCase(Locale.ROOT));
            Files.createDirectories(groupDirectory);
            writeString(groupDirectory.resolve("_index.md"), generator.group(group));
            group.settings()
                    .stream()
                    .filter(ConfigurationSetting::generateDocs)
                    .forEach(setting ->
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

    private static void forAllConfigurations(
            final Function<ConfigurationGroup, Stream<TypeSpec>> generator,
            final Consumer<TypeSpec> writer) {
        AllConfigurations.allConfigurationGroups().flatMap(generator).forEach(writer);
    }

}
