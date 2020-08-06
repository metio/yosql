package wtf.metio.yosql.maven;

import com.squareup.javapoet.ClassName;
import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.ResultConfiguration;

/**
 * Configures how results are generated.
 */
public class Results {

    /**
     * The simple name of the generated result row class (default: <strong>ResultRow</strong>).
     */
    @Parameter(required = true, defaultValue = "ResultRow")
    private String resultRow;

    /**
     * The simple name of the generated result state class (default: <strong>ResultState</strong>).
     */
    @Parameter(required = true, defaultValue = "ResultState")
    private String resultState;

    ResultConfiguration asConfiguration(final String utilPackage) {
        return ResultConfiguration.builder()
                .setResultRowClass(ClassName.get(utilPackage, resultRow))
                .setResultStateClass(ClassName.get(utilPackage, resultState))
                .build();
    }

}
