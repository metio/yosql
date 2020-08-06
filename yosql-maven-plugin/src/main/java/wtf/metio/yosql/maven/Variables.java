package wtf.metio.yosql.maven;

import wtf.metio.yosql.model.configuration.VariableConfiguration;
import wtf.metio.yosql.model.options.VariableTypeOptions;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Configures how variables are generated.
 */
public class Variables {

    VariableConfiguration asConfiguration() {
        return VariableConfiguration.builder()
                .setModifiers(List.of(Modifier.FINAL)) // TODO: configure w/ Maven
                .setVariableType(VariableTypeOptions.TYPE) // TODO: configure w/ Maven
                .build();
    }

}
