package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.StatementConfiguration;
import wtf.metio.yosql.model.options.StatementInRepositoryOptions;

/**
 * Configures how SQL statements are handled.
 */
public class Statements {

    /**
     * Controls whether the SQL statements should be inlined in the generated repositories or loaded at options
     * (default: <strong>inline</strong>). Other possible value is <strong>load</strong>.
     */
    @Parameter(required = true, defaultValue = "inline")
    private String repositorySqlStatements;

    StatementConfiguration asConfiguration() {
        return StatementConfiguration.builder()
                .setEmbed(StatementInRepositoryOptions.INLINE_CONCAT) // TODO: configure w/ Maven
                .build();
    }

}
