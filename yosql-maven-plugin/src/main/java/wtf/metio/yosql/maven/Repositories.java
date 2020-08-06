package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.RepositoryConfiguration;

/**
 * Configures how repositories are generated.
 */
public class Repositories {

    /**
     * The repository name suffix to use for all generated repositories (default: <strong>Repository</strong>).
     */
    @Parameter(required = true, defaultValue = "Repository")
    private String nameSuffix;

    /**
     * Controls whether an interface should be generated for each generated repository. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean generateInterfaces;

    /**
     * Controls whether the SQL statements should be inlined in the generated repositories or loaded at options
     * (default: <strong>inline</strong>). Other possible value is <strong>load</strong>.
     */
    @Parameter(required = true, defaultValue = "inline")
    private String repositorySqlStatements;

    RepositoryConfiguration asConfiguration() {
        return RepositoryConfiguration.builder()
                .setRepositoryGenerateInterface(generateInterfaces)
                .setRepositoryNameSuffix(nameSuffix)
                .build();
    }

}
