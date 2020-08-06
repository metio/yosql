package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.JavaConfiguration;

/**
 * Configures Java version and related settings.
 */
public class Java {

    /**
     * The target Java source version (default: <strong>1.8</strong>). Use either '1.X' syntax or just '8', '11'
     * and so on.
     */
    @Parameter(required = true, defaultValue = "1.8")
    private String java;

    JavaConfiguration asConfiguration() {
        return JavaConfiguration.builder()
                .setTargetVersion(1)
                .setUseVar(true) // TODO: configure w/ Maven
                .setUseRecords(true) // TODO: configure w/ Maven
                .build();
    }

}
