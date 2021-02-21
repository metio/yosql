package wtf.metio.yosql.internals.model.generator;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, executionStrategy = "always")
public class ModelGenerator extends AbstractMojo {

    @Parameter(defaultValue = "maven")
    String type;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() {
        final var baseOutputDirectory = Paths.get(project.getBuild().getDirectory())
                .resolve("generated-sources");
        final var outputName = String.format("yosql-%s-model", type);
        final var outputDirectory = baseOutputDirectory.resolve(outputName);

        if ("maven".equalsIgnoreCase(type)) {
            MavenGenerator.writeMavenModels(writeTypes("wtf.metio.yosql.tooling.maven", outputDirectory));
        } else if ("cli".equalsIgnoreCase(type)) {
            CliGenerator.writeCliModels(writeTypes("wtf.metio.yosql.tooling.cli", outputDirectory));
        }
    }

    private static Consumer<TypeSpec> writeTypes(final String targetPackageName, final Path targetDirectory) {
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
