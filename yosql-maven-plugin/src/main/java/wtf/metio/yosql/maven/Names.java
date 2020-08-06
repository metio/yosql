package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.NameConfiguration;

/**
 * Configures general names used during code generation.
 */
public class Names {

    /**
     * The base package name for all generated classes (default: <strong>com.example.persistence</strong>).
     */
    @Parameter(required = true, defaultValue = "com.example.persistence")
    private String basePackageName;

    /**
     * The package name for utility classes (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String utilityPackageName;

    /**
     * The package name for utility classes (default: <strong>converter</strong>).
     */
    @Parameter(required = true, defaultValue = "converter")
    private String converterPackageName;

    NameConfiguration asConfiguration() {
        return NameConfiguration.builder()
                .setBasePackageName(basePackageName)
                .setUtilityPackageName(utilityPackageName)
                .setConverterPackageName(converterPackageName)
                .build();
    }

    String getUtilityPackageName() {
        return basePackageName + "." + utilityPackageName;
    }

}
