/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at http://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.internals.model.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.internals.model.generator.cli.CliGenerator;
import wtf.metio.yosql.internals.model.generator.immutables.ImmutablesGenerator;
import wtf.metio.yosql.internals.model.generator.maven.MavenGenerator;
import wtf.metio.yosql.models.meta.ConfigurationGroup;
import wtf.metio.yosql.models.meta.data.AllConfigurations;
import wtf.metio.yosql.models.meta.data.Runtime;
import wtf.metio.yosql.models.meta.data.Sql;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Function;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, executionStrategy = "always")
public class ModelGenerator extends AbstractMojo {

    @Parameter(defaultValue = "immutables")
    String type;

    @Parameter(defaultValue = "wtf.metio.yosql.models.immutables")
    String immutablesBasePackage;

    @Parameter(defaultValue = "wtf.metio.yosql.tooling.maven")
    String mavenBasePackage;

    @Parameter(defaultValue = "wtf.metio.yosql.models.cli")
    String cliBasePackage;

    @Parameter(defaultValue = "wtf.metio.yosql.models.ant")
    String antBasePackage;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        final var baseOutputDirectory = Paths.get(project.getBuild().getDirectory())
                .resolve("generated-sources");
        final var outputDirectory = baseOutputDirectory.resolve("yosql");

        if ("immutables".equalsIgnoreCase(type)) {
            final var generator = new ImmutablesGenerator(immutablesBasePackage);
            final var writer = typeWriter(immutablesBasePackage, outputDirectory);
            forAllConfigurations(generator, writer);
            writer.accept(generator.apply(Runtime.configurationGroup(immutablesBasePackage)));
            writer.accept(generator.apply(Sql.configurationGroup()));
        } else if ("maven".equalsIgnoreCase(type)) {
            final var generator = new MavenGenerator(immutablesBasePackage);
            final var writer = typeWriter(mavenBasePackage, outputDirectory);
            forAllConfigurations(generator, writer);
        } else if ("cli".equalsIgnoreCase(type)) {
            final var generator = new CliGenerator(immutablesBasePackage);
            final var writer = typeWriter(cliBasePackage, outputDirectory);
            forAllConfigurations(generator, writer);
        } else if ("ant".equalsIgnoreCase(type)) {
            // CliGenerator.writeCliModels(typeWriter(antBasePackage, outputDirectory));
        }
    }

    private static void forAllConfigurations(final Function<ConfigurationGroup, TypeSpec> generator, final Consumer<TypeSpec> writer) {
        AllConfigurations.allConfigurationGroups()
                .stream().map(generator).forEach(writer);
    }

    private static Consumer<TypeSpec> typeWriter(final String targetPackageName, final Path targetDirectory) {
        return typeSpec -> {
            try {
                JavaFile.builder(targetPackageName, typeSpec)
                        .build()
                        .writeTo(targetDirectory);
            } catch (final IOException exception) {
                throw new RuntimeException(exception);
            }
        };
    }

}
