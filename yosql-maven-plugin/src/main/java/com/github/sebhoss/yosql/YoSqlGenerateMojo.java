package com.github.sebhoss.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.maven.model.Dependency;
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
 * <li>compile statement inline vs. load at runtime (from a different module)
 * </li>
 * <li>generate converter for result</li>
 * <li>configure global converters in POM</li>
 * <li>error log w/ full SQL statement (+ parameter substitution), so users can
 * write that into a different log file.</li>
 * <li>allow to overwrite error log messages through fixed location of
 * .properties file</li>
 * <li>: provide default.properties for error logging (part of this project),
 * allow to overwrite those defaults w/ user-project settings</li>
 * <li>sql file/repository layout: directory as repo with single statement files
 * or multi statement files as repo.</li>
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
     * The package name for utilities (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String                       utilityPackageName;

    /**
     * The base package name for all generated classes (default:
     * <strong>com.example.persistence</strong>).
     */
    @Parameter(required = true, defaultValue = "com.example.persistence")
    private String                       basePackageName;

    /**
     * Controls whether the SQL statements should be inlined in the generated
     * repositories or loaded at runtime (default: <strong>inline</strong>).
     * Other possible value is <strong>load</strong>.
     */
    @Parameter(required = true, defaultValue = "inline")
    private String                       repositorySqlStatements;

    /**
     * Controls whether the generated repositories should contain
     * <em>standard</em> methods that. Standard methods execute depending on the
     * type of the query and could either be a single 'executeQuery' on a
     * PreparedStatement in case of SQL SELECT statements or a single call to
     * 'executeUpdate' for SQL UPDATE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateStandardApi;

    /**
     * Controls whether the generated repositories should contain batch methods
     * for SQL INSERT/UPDATE/DELETE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      generateBatchApi;

    /**
     * The method name prefix to apply to all batch methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       methodBatchPrefix;

    /**
     * The method name suffix to apply to all batch methods (default:
     * <strong>"Batch"</strong>).
     */
    @Parameter(required = true, defaultValue = "Batch")
    private String                       methodBatchSuffix;

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
     * defaults to <strong>true</strong>.
     */
    @Parameter(required = false)
    private Boolean                      generateRxJavaApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       methodStreamPrefix;

    /**
     * The method name suffix to apply to all stream methods (default:
     * <strong>Stream</strong>).
     */
    @Parameter(required = true, defaultValue = "Stream")
    private String                       methodStreamSuffix;

    /**
     * The method name prefix to apply to all RxJava methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                       methodRxJavaPrefix;

    /**
     * The method name suffix to apply to all RxJava methods (default:
     * <strong>Flow</strong>).
     */
    @Parameter(required = true, defaultValue = "Flow")
    private String                       methodRxJavaSuffix;

    /**
     * The method name extra to apply to all lazy stream methods (default:
     * <strong>Lazy</strong>).
     */
    @Parameter(required = true, defaultValue = "Lazy")
    private String                       methodLazyName;

    /**
     * The method name extra to apply to all eager stream methods (default:
     * <strong>Eager</strong>).
     */
    @Parameter(required = true, defaultValue = "Eager")
    private String                       methodEagerName;

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
    private String                       methodAllowedWritePrefixes;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"update, insert, delete, create, write, add, remove,
     * merge"</strong>).
     */
    @Parameter(required = true, defaultValue = "select,read,query,find")
    private String                       methodAllowedReadPrefixes;

    /**
     * Controls whether method names are validated according to
     * <strong>allowedReadPrefixes</strong> and
     * <strong>allowedWritePrefixes</strong> (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                      methodValidateNamePrefixes;

    /**
     * The groupId to match for automatic RxJava detection (default:
     * <strong>"io.reactivex.rxjava2"</strong>).
     */
    @Parameter(required = true, defaultValue = "io.reactivex.rxjava2")
    private String                       rxJavaGroupId;

    /**
     * The artifactId to match for automatic RxJava detection (default:
     * <strong>"rxjava"</strong>).
     */
    @Parameter(required = true, defaultValue = "rxjava")
    private String                       rxJavaArtifactId;

    /**
     * The separator to split SQL statements inside a single .sql file (default:
     * <strong>";"</strong>).
     */
    @Parameter(required = true, defaultValue = ";")
    private String                       sqlStatementSeparator;

    /**
     * The separator to split SQL statements inside a single .sql file (default:
     * <strong>utf-8</strong>).
     */
    @Parameter(required = true, defaultValue = "UTF-8")
    private String                       sqlFilesCharset;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private List<ConverterConfiguration> converters;

    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    private MavenProject                 project;

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
        runtimeConfig.setLogger(() -> getLog());

        runtimeConfig.setOutputBaseDirectory(outputBaseDirectory);
        runtimeConfig.setBasePackageName(basePackageName);
        runtimeConfig.setUtilityPackageName(utilityPackageName);
        runtimeConfig.setSqlStatementSeparator(sqlStatementSeparator);
        runtimeConfig.setSqlFilesCharset(sqlFilesCharset);

        runtimeConfig.setRepositoryNameSuffix(repositoryNameSuffix);
        runtimeConfig.setRepositorySqlStatements(repositorySqlStatements);

        runtimeConfig.setGenerateStandardApi(generateStandardApi);
        runtimeConfig.setGenerateBatchApi(generateBatchApi);
        runtimeConfig.setGenerateStreamEagerApi(generateEagerStreamApi);
        runtimeConfig.setGenerateStreamLazyApi(generateLazyStreamApi);
        if (generateRxJavaApi == null) {
            runtimeConfig.setGenerateRxJavaApi(project.getDependencies().stream().anyMatch(isRxJava2()));
        } else {
            runtimeConfig.setGenerateRxJavaApi(generateRxJavaApi);
        }

        runtimeConfig.setMethodBatchPrefix(methodBatchPrefix);
        runtimeConfig.setMethodBatchSuffix(methodBatchSuffix);
        runtimeConfig.setMethodRxJavaPrefix(methodRxJavaPrefix);
        runtimeConfig.setMethodRxJavaSuffix(methodRxJavaSuffix);
        runtimeConfig.setMethodStreamPrefix(methodStreamPrefix);
        runtimeConfig.setMethodStreamSuffix(methodStreamSuffix);
        runtimeConfig.setMethodEagerName(methodEagerName);
        runtimeConfig.setMethodLazyName(methodLazyName);

        runtimeConfig.setAllowedReadPrefixes(allowedReadPrefixes());
        runtimeConfig.setAllowedWritePrefixes(allowedWritePrefixes());
        runtimeConfig.setValidateMethodNamePrefixes(methodValidateNamePrefixes);

        final Instant postInit = Instant.now();
        getLog().debug("Time spent initializing (ms): " + Duration.between(preInit, postInit).toMillis());
    }

    private String[] allowedWritePrefixes() {
        return allowedPrefixes(methodAllowedWritePrefixes);
    }

    private String[] allowedReadPrefixes() {
        return allowedPrefixes(methodAllowedReadPrefixes);
    }

    private String[] allowedPrefixes(final String input) {
        return Arrays.stream(input.split(","))
                .filter(Objects::nonNull)
                .map(String::trim)
                .toArray(String[]::new);
    }

    private Predicate<? super Dependency> isRxJava2() {
        return dependency -> rxJavaGroupId.equals(dependency.getGroupId())
                && rxJavaArtifactId.equals(dependency.getArtifactId());
    }

    private List<SqlStatement> parseAllSqlFiles() {
        final Instant preParse = Instant.now();
        final FileSet filesToParse = Optional.ofNullable(sqlFiles)
                .orElse(YoSqlConfiguration.defaultSqlFileSet(project.getBasedir()));
        final List<SqlStatement> allStatements = fileSetResolver.resolveFiles(filesToParse)
                .flatMap(sqlFileParser::parse)
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
        allStatements.stream()
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach(codeGenerator::generateRepository);
        final Instant postGenerate = Instant.now();
        getLog().debug(String.format("Time spent generating repositories (ms): %s",
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
