package wtf.metio.yosql.internals.config.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import wtf.metio.yosql.internals.config.generator.maven.MavenGenerator;

import java.nio.file.Paths;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, executionStrategy = "always")
public class ModelGenerator extends AbstractMojo {

    @Parameter(defaultValue = "maven")
    String type;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    MavenProject project;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        final var outputDirectory = Paths.get(project.getBuild().getDirectory())
                .resolve("generated-sources")
                .resolve("yosql-maven-model");
        MavenGenerator.writeMavenModels(outputDirectory);
    }

}
