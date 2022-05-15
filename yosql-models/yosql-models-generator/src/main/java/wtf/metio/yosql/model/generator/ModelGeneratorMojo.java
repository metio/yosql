/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */

package wtf.metio.yosql.model.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.nio.file.Paths;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true, executionStrategy = "always")
public class ModelGeneratorMojo extends AbstractMojo {

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
            new ModelGenerator(outputDirectory, immutablesBasePackage, immutablesBasePackage).createImmutableModel();
        } else if ("maven".equalsIgnoreCase(type)) {
            new ModelGenerator(outputDirectory, immutablesBasePackage, mavenBasePackage).createMavenModel();
        } else if ("cli".equalsIgnoreCase(type)) {
            new ModelGenerator(outputDirectory, immutablesBasePackage, cliBasePackage).createCliModel();
        } else if ("ant".equalsIgnoreCase(type)) {
            // new ModelGenerator(outputDirectory, type, project.getVersion(), immutablesBasePackage, cliBasePackage).execute();
        } else if ("website".equalsIgnoreCase(type)) {
            new ModelGenerator(outputDirectory, immutablesBasePackage, "")
                    .createMarkdownDocumentation(project.getVersion());
        }
    }

}
