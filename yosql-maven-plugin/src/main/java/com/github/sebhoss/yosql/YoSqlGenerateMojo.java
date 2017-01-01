package com.github.sebhoss.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * TODO:
 * <ul>
 * <li>Factory/Spring/CDI/Guice(?)</li>
 * <li>Single/multiple execution</li>
 * <li>: folder structure could equal package structure + simple class name of
 * Repository as last folder</li>
 * <li>: SQL files in that last folder are methods of the Repository</li>
 * <li>: configure default suffix (e.g. Repository) in POM -> shorter folder
 * names</li>
 * <li>Plugin config < execution config < yaml config</li>
 * <li>file name as default for method/statement name</li>
 * <li>compile statement inline vs. load at runtime (from a different module)
 * </li>
 * <li>generate converter for result</li>
 * <li>configure global converters in POM</li>
 * <li>error log w/ full SQL statement (+ parameter substitution), so users can
 * write that into a different log file.</li>
 * <li>batch api (enabled by default, disabled w/ POM config + YAML per SQL
 * file)</li>
 * <li>java.util.Stream api (enabled by default, disabled w/ POM config + YAML
 * per SQL file)</li>
 * <li>allow to overwrite error log messages through fixed location of
 * .properties file</li>
 * <li>: provide default.properties for error logging (part of this project),
 * allow to overwrite those defaults w/ user-project settings</li>
 * </ul>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = false)
public class YoSqlGenerateMojo extends AbstractMojo {

    /**
     * The SQL files to load (.sql)
     */
    @Parameter(required = false)
    private final FileSet                sqlFiles = YoSqlConfiguration.defaultSqlFileSet();

    /**
     * The output directory for the resolved templates
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/yosql")
    private File                         outputBaseDirectory;

    /**
     * Controls whether the SQL statements should be compiled inlined (default:
     * <strong>yes</strong>) or loaded at runtime.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      compileInline;

    /**
     * Controls whether the generated repositories should contain batch methods
     * (default: <strong>yes</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateBatchApi;

    /**
     * The method name prefix to apply to all batch methods (default:
     * <strong>batch</strong>).
     */
    @Parameter(required = true, defaultValue = "batch")
    private String                       batchPrefix;

    /**
     * Controls whether the generated repositories should offer {@link Stream}s
     * as return types (default: <strong>yes</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateStreamApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>stream</strong>).
     */
    @Parameter(required = true, defaultValue = "stream")
    private String                       streamPrefix;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private List<ConverterConfiguration> converters;

    private final PluginErrors           pluginErrors;
    private final PluginPreconditions    preconditions;
    private final FileSetResolver        fileSetResolver;
    private final SqlFileParser          sqlFileParser;
    private final CodeGenerator          codeGenerator;

    @Inject
    YoSqlGenerateMojo(
            final PluginErrors pluginErrors,
            final PluginPreconditions preconditions,
            final FileSetResolver fileSetResolver,
            final SqlFileParser sqlFileParser,
            final CodeGenerator codeGenerator) {
        this.pluginErrors = pluginErrors;
        this.preconditions = preconditions;
        this.fileSetResolver = fileSetResolver;
        this.sqlFileParser = sqlFileParser;
        this.codeGenerator = codeGenerator;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        preconditions.assertDirectoryIsWriteable(outputBaseDirectory);

        fileSetResolver.resolveFiles(sqlFiles)
                .map(sqlFileParser::parse)
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach((name, statements) -> codeGenerator.generateRepository(
                        outputBaseDirectory.toPath().resolve(name + ".java"),
                        statements));

        if (pluginErrors.hasErrors()) {
            pluginErrors.buildError("Error during mojo execution");
        }
    }

}
