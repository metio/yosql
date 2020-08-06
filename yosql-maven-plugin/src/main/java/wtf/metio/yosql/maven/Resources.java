package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.ResourceConfiguration;

/**
 * Configures resources used during code generation.
 */
public class Resources {

    ResourceConfiguration asConfiguration() {
        return ResourceConfiguration.builder()
                .setMaxThreads(1) // TODO: configure w/ Maven
                .build();
    }

}
