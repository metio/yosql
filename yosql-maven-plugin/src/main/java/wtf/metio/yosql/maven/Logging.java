package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;
import wtf.metio.yosql.model.configuration.LoggingConfiguration;
import wtf.metio.yosql.model.options.LoggingApiOptions;

/**
 * Configures how logging is applied to generated code.
 */
public class Logging {

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the logging API based on the projects
     * dependencies). Possible other values are "jdk", "log4j", "slf4j" and "none".
     */
    @Parameter(required = true, defaultValue = "auto")
    private String loggingApi;

    /**
     * The groupId to match for automatic Log4j detection (default: <strong>"org.apache.logging.log4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.apache.logging.log4j")
    private String log4jGroupId;

    /**
     * The artifactId to match for automatic Log4j detection (default: <strong>"log4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "log4j-api")
    private String log4jArtifactId;

    /**
     * The groupId to match for automatic SLF4j detection (default: <strong>"org.slf4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.slf4j")
    private String slf4jGroupId;

    /**
     * The artifactId to match for automatic SLF4j detection (default: <strong>"slf4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "slf4j-api")
    private String slf4jArtifactId;

    LoggingConfiguration asConfiguration() {
        return LoggingConfiguration.builder()
                .setApi(determineLoggingApi())
                .build();
    }

    private LoggingApiOptions determineLoggingApi() {
        return switch (loggingApi) {
            case "auto", "slf4j" -> LoggingApiOptions.SLF4J;
            case "log4j" -> LoggingApiOptions.LOG4J;
            case "jdk" -> LoggingApiOptions.JDK;
            default -> null;
        };
    }

}
