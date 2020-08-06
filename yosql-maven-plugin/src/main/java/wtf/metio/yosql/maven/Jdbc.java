package wtf.metio.yosql.maven;

import org.apache.maven.plugins.annotations.Parameter;

/**
 * Configures how the JDBC API is used.
 */
public class Jdbc {

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter
    private JdbcNames names;

}
