package com.github.sebhoss.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private FileSet                      sqlFiles;

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
     * Controls whether the generated repositories should contain methods that
     * execute a single query (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateSingleQueryApi;

    /**
     * Controls whether the generated repositories should contain batch methods
     * (default: <strong>true</strong>).
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
     * The method name suffix to apply to all batch methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       batchSuffix;

    /**
     * Controls whether the generated repositories should offer eager
     * {@link Stream}s as return types (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateEagerStreamApi;

    /**
     * Controls whether the generated repositories should offer lazy
     * {@link Stream}s as return types (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateLazyStreamApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>stream</strong>).
     */
    @Parameter(required = true, defaultValue = "stream")
    private String                       streamPrefix;

    /**
     * The method name extra to apply to all lazy stream methods (default:
     * <strong>Lazy</strong>).
     */
    @Parameter(required = true, defaultValue = "Lazy")
    private String                       lazyName;

    /**
     * The method name extra to apply to all eager stream methods (default:
     * <strong>Eager</strong>).
     */
    @Parameter(required = true, defaultValue = "Eager")
    private String                       eagerName;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private List<ConverterConfiguration> converters;

    @Parameter(defaultValue = "${project.basedir}", readonly = true, required = true)
    private File                         basedir;

    private final PluginErrors           pluginErrors;
    private final PluginPreconditions    preconditions;
    private final FileSetResolver        fileSetResolver;
    private final SqlFileParser          sqlFileParser;
    private final CodeGenerator          codeGenerator;
    private final PluginRuntimeConfig    runtimeConfig;

    @Inject
    YoSqlGenerateMojo(
            final PluginErrors pluginErrors,
            final PluginPreconditions preconditions,
            final FileSetResolver fileSetResolver,
            final SqlFileParser sqlFileParser,
            final CodeGenerator codeGenerator,
            final PluginRuntimeConfig runtimeConfig) {
        this.pluginErrors = pluginErrors;
        this.preconditions = preconditions;
        this.fileSetResolver = fileSetResolver;
        this.sqlFileParser = sqlFileParser;
        this.codeGenerator = codeGenerator;
        this.runtimeConfig = runtimeConfig;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        preconditions.assertDirectoryIsWriteable(outputBaseDirectory);

        runtimeConfig.setBatchPrefix(batchPrefix);
        runtimeConfig.setBatchSuffix(batchSuffix);
        runtimeConfig.setCompileInline(compileInline);
        runtimeConfig.setEagerName(eagerName);
        runtimeConfig.setGenerateSingleQueryApi(generateSingleQueryApi);
        runtimeConfig.setGenerateBatchApi(generateBatchApi);
        runtimeConfig.setGenerateStreamEagerApi(generateEagerStreamApi);
        runtimeConfig.setGenerateStreamLazyApi(generateLazyStreamApi);
        runtimeConfig.setLazyName(lazyName);
        runtimeConfig.setOutputBaseDirectory(outputBaseDirectory);
        runtimeConfig.setStreamPrefix(streamPrefix);
        runtimeConfig.setUtilityPackageName("com.example.util");
        runtimeConfig.setLogger(() -> getLog());

        fileSetResolver.resolveFiles(Optional.ofNullable(sqlFiles)
                .orElse(YoSqlConfiguration.defaultSqlFileSet(basedir)))
                .map(sqlFileParser::parse)
                .sorted(compareByName())
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach((name, statements) -> codeGenerator.generateRepository(name.replace("/", "."), statements));
        codeGenerator.generateUtilities();

        if (pluginErrors.hasErrors()) {
            pluginErrors.buildError("Error during mojo execution");
        }
    }

    private Comparator<SqlStatement> compareByName() {
        return (s1, s2) -> s1.getConfiguration().getName().compareTo(s2.getConfiguration().getName());
    }

}
