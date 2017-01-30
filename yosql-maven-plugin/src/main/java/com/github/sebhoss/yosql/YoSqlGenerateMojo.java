package com.github.sebhoss.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

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
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       batchPrefix;

    /**
     * The method name suffix to apply to all batch methods (default:
     * <strong>"Batch"</strong>).
     */
    @Parameter(required = true, defaultValue = "Batch")
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
     * Controls whether the generated repositories should offer RxJava
     * {@link io.reactivex.Flowable}s as return types (default:
     * <strong>false</strong>). In case
     * <strong>io.reactivex.rxjava2:rxjava</strong> is a declared dependency,
     * default is <strong>true</strong>.
     */
    @Parameter(required = false)
    private Boolean                      generateRxJavaApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       streamPrefix;

    /**
     * The method name suffix to apply to all stream methods (default:
     * <strong>Stream</strong>).
     */
    @Parameter(required = true, defaultValue = "Stream")
    private String                       streamSuffix;

    /**
     * The method name prefix to apply to all RxJava methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       reactivePrefix;

    /**
     * The method name suffix to apply to all RxJava methods (default:
     * <strong>Flow</strong>).
     */
    @Parameter(required = true, defaultValue = "Flow")
    private String                       reactiveSuffix;

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
     * The package name for utilities (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String                       utilityPackageName;

    /**
     * The base package name for all generated classes.
     */
    @Parameter(required = true)
    private String                       basePackageName;

    /**
     * The repository name suffix to use for all generated repositories
     * (default: <strong>Repository</strong>).
     */
    @Parameter(required = true, defaultValue = "Repository")
    private String                       repositoryNameSuffix;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"update, insert, delete, create, write, add, remove,
     * merge"</strong>).
     */
    @Parameter(required = true, defaultValue = "update,insert,delete,create,write,add,remove,merge")
    private String                       allowedWritePrefixes;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"update, insert, delete, create, write, add, remove,
     * merge"</strong>).
     */
    @Parameter(required = true, defaultValue = "select,read,query,find")
    private String                       allowedReadPrefixes;

    /**
     * Controls whether method names are validated according to
     * <strong>allowedReadPrefixes</strong> and
     * <strong>allowedWritePrefixes</strong> (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      validateMethodNamePrefixes;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private List<ConverterConfiguration> converters;

    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    protected MavenProject               project;

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

        initializePluginConfig();
        final List<SqlStatement> allStatements = parseAllSqlFiles();
        generateRepositories(allStatements);
        generateUtilities(allStatements);

        if (pluginErrors.hasErrors()) {
            pluginErrors.buildError("Error during code generation");
        }
    }

    private void initializePluginConfig() {
        final Instant preInit = Instant.now();
        runtimeConfig.setBatchPrefix(batchPrefix);
        runtimeConfig.setBatchSuffix(batchSuffix);
        runtimeConfig.setReactivePrefix(reactivePrefix);
        runtimeConfig.setReactiveSuffix(reactiveSuffix);
        runtimeConfig.setRepositoryNameSuffix(repositoryNameSuffix);
        runtimeConfig.setCompileInline(compileInline);
        runtimeConfig.setEagerName(eagerName);
        runtimeConfig.setGenerateSingleQueryApi(generateSingleQueryApi);
        runtimeConfig.setGenerateBatchApi(generateBatchApi);
        runtimeConfig.setGenerateStreamEagerApi(generateEagerStreamApi);
        runtimeConfig.setGenerateStreamLazyApi(generateLazyStreamApi);
        runtimeConfig.setGenerateRxJavaApi(generateRxJavaApi == null ? false : generateRxJavaApi);
        runtimeConfig.setLazyName(lazyName);
        runtimeConfig.setOutputBaseDirectory(outputBaseDirectory);
        runtimeConfig.setStreamPrefix(streamPrefix);
        runtimeConfig.setStreamSuffix(streamSuffix);
        runtimeConfig.setUtilityPackageName(utilityPackageName);
        runtimeConfig.setBasePackageName(basePackageName);
        runtimeConfig.setLogger(() -> getLog());
        runtimeConfig.setAllowedReadPrefixes(allowedReadPrefixes.split(","));
        runtimeConfig.setAllowedWritePrefixes(allowedWritePrefixes.split(","));
        runtimeConfig.setValidateMethodNamePrefixes(validateMethodNamePrefixes);
        if (generateRxJavaApi == null) {
            runtimeConfig.setGenerateRxJavaApi(project.getDependencies().stream()
                    .filter(dependency -> "io.reactivex.rxjava2".equals(dependency.getGroupId()))
                    .anyMatch(dependency -> "rxjava".equals(dependency.getArtifactId())));
        }
        final Instant postInit = Instant.now();
        getLog().debug("Time spent initializing (ms): " + Duration.between(preInit, postInit).toMillis());
    }

    private List<SqlStatement> parseAllSqlFiles() {
        final Instant preParse = Instant.now();
        final FileSet filesToParse = Optional.ofNullable(sqlFiles)
                .orElse(YoSqlConfiguration.defaultSqlFileSet(project.getBasedir()));
        final List<SqlStatement> allStatements = fileSetResolver.resolveFiles(filesToParse)
                .map(sqlFileParser::parse)
                .sorted(Comparator.comparing(statement -> statement.getConfiguration().getName()))
                .collect(Collectors.toList());
        final Instant postParse = Instant.now();
        getLog().debug(String.format("Time spent parsing [%s] statements (ms): %s",
                allStatements.size(),
                Duration.between(preParse, postParse).toMillis()));
        return allStatements;
    }

    private void generateRepositories(final List<SqlStatement> allStatements) {
        final Instant preGenerate = Instant.now();
        final Map<String, List<SqlStatement>> repositories = allStatements.stream()
                .collect(groupingBy(SqlStatement::getRepository));
        repositories.forEach(codeGenerator::generateRepository);
        final Instant postGenerate = Instant.now();
        getLog().debug(String.format("Time spent generating [%s] repositories (ms): %s",
                repositories.size(),
                Duration.between(preGenerate, postGenerate).toMillis()));
    }

    private void generateUtilities(final List<SqlStatement> allStatements) {
        final Instant preGenerate = Instant.now();
        codeGenerator.generateUtilities(allStatements);
        final Instant postGenerate = Instant.now();
        getLog().debug("Time spent generating utilities (ms): "
                + Duration.between(preGenerate, postGenerate).toMillis());
    }

}
