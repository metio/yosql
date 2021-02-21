package wtf.metio.yosql.internals.model.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.internals.meta.model.ConfigurationGroup;
import wtf.metio.yosql.internals.meta.model.data.AllConfigurations;
import wtf.metio.yosql.internals.model.generator.immutables.ImmutablesGenerator;
import wtf.metio.yosql.internals.model.generator.maven.MavenGenerator;

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
        } else if ("maven".equalsIgnoreCase(type)) {
            final var generator = new MavenGenerator(immutablesBasePackage);
            final var writer = typeWriter(mavenBasePackage, outputDirectory);
            forAllConfigurations(generator, writer);
        } else if ("cli".equalsIgnoreCase(type)) {
            // CliGenerator.writeCliModels(typeWriter(cliBasePackage, outputDirectory));
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
