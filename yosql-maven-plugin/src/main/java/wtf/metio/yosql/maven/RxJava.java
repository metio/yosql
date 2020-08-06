package wtf.metio.yosql.maven;

import com.squareup.javapoet.ClassName;
import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.RxJavaConfiguration;

/**
 * Configures how RxJava is used.
 */
public class RxJava {

    /**
     * The simple name of the generated flow state class (default: <strong>FlowState</strong>).
     */
    @Parameter(required = true, defaultValue = "FlowState")
    private String flowState;

    /**
     * The groupId to match for automatic RxJava detection (default: <strong>"io.reactivex.rxjava2"</strong>).
     */
    @Parameter(required = true, defaultValue = "io.reactivex.rxjava2")
    private String groupId;

    /**
     * The artifactId to match for automatic RxJava detection (default: <strong>"rxjava"</strong>).
     */
    @Parameter(required = true, defaultValue = "rxjava")
    private String artifactId;

    RxJavaConfiguration asConfiguration(final String utilPackage) {
        return RxJavaConfiguration.builder()
                .setFlowStateClass(ClassName.get(utilPackage, flowState))
                .build();
    }

}
