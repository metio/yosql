package wtf.metio.yosql.internals.config.generator.maven;

import com.squareup.javapoet.JavaFile;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.IOException;
import java.nio.file.Path;

public final class MavenGenerator {

    public static void writeMavenModels(final Path targetDirectory) throws MojoExecutionException {
        try {
            JavaFile.builder("wtf.metio.yosql.internals.model.maven", MavenAnnotations.asTypeSpec())
                    .build()
                    .writeTo(targetDirectory);
        } catch (final IOException exception) {
            throw new MojoExecutionException(exception, "IO problem", "Could not write models for Maven");
        }
    }

    private MavenGenerator() {
        // factory class
    }

}
