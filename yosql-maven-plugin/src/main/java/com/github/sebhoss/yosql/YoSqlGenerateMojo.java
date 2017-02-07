package com.github.sebhoss.yosql;

import static java.util.stream.Collectors.groupingBy;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
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

import com.github.sebhoss.yosql.generator.FlowStateGenerator;
import com.github.sebhoss.yosql.generator.ResultRowGenerator;
import com.github.sebhoss.yosql.generator.ResultStateGenerator;
import com.github.sebhoss.yosql.generator.ToResultRowConverterGenerator;
import com.squareup.javapoet.ClassName;

/**
 * TODO:
 * <ul>
 * <li>Factory/Spring/CDI/Guice(?)</li>
 * <li>error log w/ full SQL statement (+ parameter substitution), so users can
 * write that into a different log file.</li>
 * <li>allow to overwrite error log messages through fixed location of
 * .properties file</li>
 * <li>: provide default.properties for error logging (part of this project),
 * allow to overwrite those defaults w/ user-project settings</li>
 * <li>sql file/repository layout: directory as repo with single statement files
 * or multi statement files as repo.</li>
 * <li>support single DB vendor statements that do nothing for all other
 * vendors</li>
 * <li>support unnamed params ('?') as varargs argument</li>
 * <li>parameter converter</li>
 * <li>RETURNING statements</li>
 * <li>support symbols: "!" for write, "<" for returning, none (by default) for
 * read
 * <li>Use http://simpleflatmapper.org/ for mapping between results & beans</li>
 * <li>Write perf test for
 * https://github.com/bwajtr/java-persistence-frameworks-comparison</li>
 * <li>SQL statement name converter to align with Java naming rules</li>
 * </ul>
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class YoSqlGenerateMojo extends AbstractMojo {

    private static final String                         RESULT_ROW_CONVERTER = "resultRowConverter";

    /**
     * The SQL files to load (.sql)
     */
    @Parameter(required = false)
    private FileSet                                     sqlFiles;

    /**
     * The output directory for the generated classes
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/yosql")
    private File                                        outputBaseDirectory;

    /**
     * The package name for utility classes (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String                                      utilityPackageName;

    /**
     * The base package name for all generated classes (default:
     * <strong>com.example.persistence</strong>).
     */
    @Parameter(required = true, defaultValue = "com.example.persistence")
    private String                                      basePackageName;

    /**
     * Controls whether the SQL statements should be inlined in the generated
     * repositories or loaded at runtime (default: <strong>inline</strong>).
     * Other possible value is <strong>load</strong>. TODO: implement 'load'
     */
    @Parameter(required = true, defaultValue = "inline")
    private String                                      repositorySqlStatements;

    /**
     * Controls whether the generated repositories should contain
     * <em>standard</em> methods that. Standard methods execute depending on the
     * type of the query and could either be a single 'executeQuery' on a
     * PreparedStatement in case of SQL SELECT statements or a single call to
     * 'executeUpdate' for SQL UPDATE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodStandardApi;

    /**
     * Controls whether the generated repositories should contain batch methods
     * for SQL INSERT/UPDATE/DELETE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodBatchApi;

    /**
     * The method name prefix to apply to all batch methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                                      methodBatchPrefix;

    /**
     * The method name suffix to apply to all batch methods (default:
     * <strong>"Batch"</strong>).
     */
    @Parameter(required = true, defaultValue = "Batch")
    private String                                      methodBatchSuffix;

    /**
     * Controls whether an eager {@link Stream} based method should be generated
     * (default: <strong>true</strong>). If the target Java version is set to
     * anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodEagerStreamApi;

    /**
     * Controls whether a lazy {@link Stream} based method should be generated
     * (default: <strong>true</strong>). If the target Java version is set to
     * anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodLazyStreamApi;

    /**
     * Controls whether a RxJava {@link io.reactivex.Flowable} based method
     * should be generated (default: <strong>false</strong>). In case
     * <strong>io.reactivex.rxjava2:rxjava</strong> is a declared dependency,
     * defaults to <strong>true</strong>.
     */
    @Parameter(required = false)
    private Boolean                                     methodRxJavaApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                                      methodStreamPrefix;

    /**
     * The method name suffix to apply to all stream methods (default:
     * <strong>Stream</strong>).
     */
    @Parameter(required = true, defaultValue = "Stream")
    private String                                      methodStreamSuffix;

    /**
     * The method name prefix to apply to all RxJava methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                                      methodRxJavaPrefix;

    /**
     * The method name suffix to apply to all RxJava methods (default:
     * <strong>Flow</strong>).
     */
    @Parameter(required = true, defaultValue = "Flow")
    private String                                      methodRxJavaSuffix;

    /**
     * The method name extra to apply to all lazy stream methods (default:
     * <strong>Lazy</strong>).
     */
    @Parameter(required = true, defaultValue = "Lazy")
    private String                                      methodLazyName;

    /**
     * The method name extra to apply to all eager stream methods (default:
     * <strong>Eager</strong>).
     */
    @Parameter(required = true, defaultValue = "Eager")
    private String                                      methodEagerName;

    /**
     * The repository name suffix to use for all generated repositories
     * (default: <strong>Repository</strong>).
     */
    @Parameter(required = true, defaultValue = "Repository")
    private String                                      repositoryNameSuffix;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"update, insert, delete, create, write, add, remove,
     * merge,drop"</strong>).
     */
    @Parameter(required = true, defaultValue = "update,insert,delete,create,write,add,remove,merge,drop")
    private String                                      methodAllowedWritePrefixes;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"select,read,query,find"</strong>).
     */
    @Parameter(required = true, defaultValue = "select,read,query,find")
    private String                                      methodAllowedReadPrefixes;

    /**
     * Controls whether method names are validated according to
     * <strong>methodAllowedReadPrefixes</strong> and
     * <strong>methodAllowedWritePrefixes</strong> (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodValidateNamePrefixes;

    /**
     * The groupId to match for automatic RxJava detection (default:
     * <strong>"io.reactivex.rxjava2"</strong>).
     */
    @Parameter(required = true, defaultValue = "io.reactivex.rxjava2")
    private String                                      rxJavaGroupId;

    /**
     * The artifactId to match for automatic RxJava detection (default:
     * <strong>"rxjava"</strong>).
     */
    @Parameter(required = true, defaultValue = "rxjava")
    private String                                      rxJavaArtifactId;

    /**
     * The separator to split SQL statements inside a single .sql file (default:
     * <strong>";"</strong>).
     */
    @Parameter(required = true, defaultValue = ";")
    private String                                      sqlStatementSeparator;

    /**
     * The charset to use while reading .sql files (default:
     * <strong>UTF-8</strong>).
     */
    @Parameter(required = true, defaultValue = "UTF-8")
    private String                                      sqlFilesCharset;

    /**
     * The target Java source version (default: <strong>1.8</strong>). A value
     * lower than 1.8 disables the generation of the 'java.util.Stream' based
     * API.
     */
    @Parameter(required = true, defaultValue = "1.8")
    private String                                      java;

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the
     * logging API based on the projects dependencies). Possible other values
     * are "jdk", "log4j", "slf4j" and "none".
     */
    @Parameter(required = true, defaultValue = "auto")
    private String                                      loggingApi;

    /**
     * Whether generated methods should catch SqlExceptions and rethrow them as
     * RuntimeExceptions (default: <strong>true</strong>). If set to
     * <strong>false</strong>, this will cause methods to declare that they
     * throw a checked exception which in turn will force all its users to
     * handle the exception.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                                     methodCatchAndRethrow;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private final List<ParameterConverterConfiguration> parameterConverters  = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private final List<ResultRowConverter>              resultRowConverters  = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is
     * specified for a statement. Can be either the alias or fully-qualified
     * name of a converter. Default 'resultRow'.
     */
    @Parameter(required = true, defaultValue = RESULT_ROW_CONVERTER)
    private String                                      defaultRowConverter;

    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    private MavenProject                                project;

    private final PluginErrors                          pluginErrors;
    private final PluginPreconditions                   preconditions;
    private final FileSetResolver                       fileSetResolver;
    private final SqlFileParser                         sqlFileParser;
    private final CodeGenerator                         codeGenerator;
    private final PluginRuntimeConfig                   runtimeConfig;
    // private final PlexusContainer beanLocator;

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
                allStatements.size(), Duration.between(preParse, postParse).toMillis()));
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

    private void initializePluginConfig() {
        final Instant preInit = Instant.now();
        final int parsedJavaVersion = Integer.parseInt(java.substring(java.length() - 1, java.length()));

        runtimeConfig.setLogger(() -> getLog());

        runtimeConfig.setOutputBaseDirectory(outputBaseDirectory);
        runtimeConfig.setBasePackageName(basePackageName);
        runtimeConfig.setUtilityPackageName(utilityPackageName);
        runtimeConfig.setSqlStatementSeparator(sqlStatementSeparator);
        runtimeConfig.setSqlFilesCharset(sqlFilesCharset);

        runtimeConfig.setRepositoryNameSuffix(repositoryNameSuffix);
        runtimeConfig.setRepositorySqlStatements(repositorySqlStatements);

        runtimeConfig.setGenerateStandardApi(methodStandardApi);
        runtimeConfig.setGenerateBatchApi(methodBatchApi);
        runtimeConfig.setGenerateStreamEagerApi(parsedJavaVersion > 7 ? methodEagerStreamApi : false);
        runtimeConfig.setGenerateStreamLazyApi(parsedJavaVersion > 7 ? methodLazyStreamApi : false);
        if (methodRxJavaApi == null) {
            runtimeConfig.setGenerateRxJavaApi(project.getDependencies().stream().anyMatch(isRxJava2()));
        } else {
            runtimeConfig.setGenerateRxJavaApi(methodRxJavaApi);
        }
        if (loggingApi == null || "auto".equals(loggingApi.toLowerCase())) {
            if (project.getDependencies().stream().anyMatch(isLog4J())) {
                runtimeConfig.setLoggingApi(LoggingAPI.LOG4J);
            } else if (project.getDependencies().stream().anyMatch(isSlf4j())) {
                runtimeConfig.setLoggingApi(LoggingAPI.SLF4J);
            } else {
                runtimeConfig.setLoggingApi(LoggingAPI.JDK);
            }
        } else {
            try {
                runtimeConfig.setLoggingApi(LoggingAPI.valueOf(loggingApi.toUpperCase()));
            } catch (final IllegalArgumentException exception) {
                runtimeConfig.setLoggingApi(LoggingAPI.NONE);
                pluginErrors.add(exception);
            }
        }

        runtimeConfig.setMethodBatchPrefix(methodBatchPrefix);
        runtimeConfig.setMethodBatchSuffix(methodBatchSuffix);
        runtimeConfig.setMethodRxJavaPrefix(methodRxJavaPrefix);
        runtimeConfig.setMethodRxJavaSuffix(methodRxJavaSuffix);
        runtimeConfig.setMethodStreamPrefix(methodStreamPrefix);
        runtimeConfig.setMethodStreamSuffix(methodStreamSuffix);
        runtimeConfig.setMethodEagerName(methodEagerName);
        runtimeConfig.setMethodLazyName(methodLazyName);
        runtimeConfig.setMethodCatchAndRethrow(methodCatchAndRethrow);

        runtimeConfig.setAllowedReadPrefixes(allowedReadPrefixes());
        runtimeConfig.setAllowedWritePrefixes(allowedWritePrefixes());
        runtimeConfig.setValidateMethodNamePrefixes(methodValidateNamePrefixes);

        final String utilPackage = basePackageName + "." + utilityPackageName;
        runtimeConfig.setFlowStateClass(ClassName.get(utilPackage, FlowStateGenerator.FLOW_STATE_CLASS_NAME));
        runtimeConfig.setResultStateClass(ClassName.get(utilPackage, ResultStateGenerator.RESULT_STATE_CLASS_NAME));

        final ResultRowConverter toResultRow = new ResultRowConverter();
        toResultRow.setAlias(RESULT_ROW_CONVERTER);
        toResultRow.setResultType(utilPackage + "." + ResultRowGenerator.RESULT_ROW_CLASS_NAME);
        toResultRow.setConverterType(utilPackage + "."
                + ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME);
        resultRowConverters.add(toResultRow);
        runtimeConfig.setResultRowConverters(resultRowConverters);
        runtimeConfig.setDefaultRowConverter(defaultRowConverter);

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

    private Predicate<? super Dependency> isLog4J() {
        return dependency -> rxJavaGroupId.equals(dependency.getGroupId())
                && rxJavaArtifactId.equals(dependency.getArtifactId());
    }

    private Predicate<? super Dependency> isSlf4j() {
        return dependency -> rxJavaGroupId.equals(dependency.getGroupId())
                && rxJavaArtifactId.equals(dependency.getArtifactId());
    }

}
