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
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Generated;
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

import com.github.sebhoss.yosql.generator.raw_jdbc.RawJdbcRepositoryGenerator;
import com.github.sebhoss.yosql.generator.utils.DefaultUtilitiesGenerator;
import com.github.sebhoss.yosql.generator.utils.FlowStateGenerator;
import com.github.sebhoss.yosql.generator.utils.ResultRowGenerator;
import com.github.sebhoss.yosql.generator.utils.ResultStateGenerator;
import com.github.sebhoss.yosql.generator.utils.ToResultRowConverterGenerator;
import com.github.sebhoss.yosql.model.LoggingAPI;
import com.github.sebhoss.yosql.model.ParameterConverter;
import com.github.sebhoss.yosql.model.ResultRowConverter;
import com.github.sebhoss.yosql.model.SqlStatement;
import com.github.sebhoss.yosql.parser.FileSetResolver;
import com.github.sebhoss.yosql.parser.SqlFileParser;
import com.github.sebhoss.yosql.plugin.PluginConfig;
import com.github.sebhoss.yosql.plugin.PluginErrors;
import com.github.sebhoss.yosql.plugin.PluginPreconditions;
import com.squareup.javapoet.ClassName;

/**
 * The *generate* goal generates Java code based on SQL files.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, threadSafe = true)
public class YoSqlGenerateMojo extends AbstractMojo {

    private static final String              RESULT_ROW_CONVERTER = "resultRowConverter";

    /**
     * The SQL files to load (.sql)
     */
    @Parameter(required = false)
    private FileSet                          sqlFiles;

    /**
     * The output directory for the generated classes
     */
    @Parameter(required = true, defaultValue = "${project.build.directory}/generated-sources/yosql")
    private File                             outputBaseDirectory;

    /**
     * The package name for utility classes (default: <strong>util</strong>).
     */
    @Parameter(required = true, defaultValue = "util")
    private String                           utilityPackageName;

    /**
     * The package name for utility classes (default:
     * <strong>converter</strong>).
     */
    @Parameter(required = true, defaultValue = "converter")
    private String                           converterPackageName;

    /**
     * The base package name for all generated classes (default:
     * <strong>com.example.persistence</strong>).
     */
    @Parameter(required = true, defaultValue = "com.example.persistence")
    private String                           basePackageName;

    /**
     * Controls whether the SQL statements should be inlined in the generated
     * repositories or loaded at runtime (default: <strong>inline</strong>).
     * Other possible value is <strong>load</strong>.
     */
    @Parameter(required = true, defaultValue = "inline")
    private String                           repositorySqlStatements;

    /**
     * Controls whether the generated repositories should contain
     * <em>standard</em> methods that. Standard methods execute depending on the
     * type of the query and could either be a single 'executeQuery' on a
     * PreparedStatement in case of SQL SELECT statements or a single call to
     * 'executeUpdate' for SQL UPDATE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodStandardApi;

    /**
     * Controls whether the generated repositories should contain batch methods
     * for SQL INSERT/UPDATE/DELETE statements. (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodBatchApi;

    /**
     * The method name prefix to apply to all batch methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                           methodBatchPrefix;

    /**
     * The method name suffix to apply to all batch methods (default:
     * <strong>"Batch"</strong>).
     */
    @Parameter(required = true, defaultValue = "Batch")
    private String                           methodBatchSuffix;

    /**
     * Controls whether an eager {@link Stream} based method should be generated
     * (default: <strong>true</strong>). If the target Java version is set to
     * anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodStreamEagerApi;

    /**
     * Controls whether a lazy {@link Stream} based method should be generated
     * (default: <strong>true</strong>). If the target Java version is set to
     * anything below 1.8, defaults to <strong>false</strong>
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodStreamLazyApi;

    /**
     * Controls whether a RxJava {@link io.reactivex.Flowable} based method
     * should be generated (default: <strong>false</strong>). In case
     * <strong>io.reactivex.rxjava2:rxjava</strong> is a declared dependency,
     * defaults to <strong>true</strong>.
     */
    @Parameter(required = false)
    private Boolean                          methodRxJavaApi;

    /**
     * The method name prefix to apply to all stream methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                           methodStreamPrefix;

    /**
     * The method name suffix to apply to all stream methods (default:
     * <strong>Stream</strong>).
     */
    @Parameter(required = true, defaultValue = "Stream")
    private String                           methodStreamSuffix;

    /**
     * The method name prefix to apply to all RxJava methods (default:
     * <strong>""</strong>).
     */
    @Parameter(required = false, defaultValue = "")
    private String                           methodRxJavaPrefix;

    /**
     * The method name suffix to apply to all RxJava methods (default:
     * <strong>Flow</strong>).
     */
    @Parameter(required = true, defaultValue = "Flow")
    private String                           methodRxJavaSuffix;

    /**
     * The method name extra to apply to all lazy stream methods (default:
     * <strong>Lazy</strong>).
     */
    @Parameter(required = true, defaultValue = "Lazy")
    private String                           methodLazyName;

    /**
     * The method name extra to apply to all eager stream methods (default:
     * <strong>Eager</strong>).
     */
    @Parameter(required = true, defaultValue = "Eager")
    private String                           methodEagerName;

    /**
     * The repository name suffix to use for all generated repositories
     * (default: <strong>Repository</strong>).
     */
    @Parameter(required = true, defaultValue = "Repository")
    private String                           repositoryNameSuffix;

    /**
     * Controls whether an interface should be generated for each generated
     * repository. (default: <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          repositoryGenerateInterface;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"update, insert, delete, create, write, add, remove,
     * merge,drop"</strong>).
     */
    @Parameter(required = true, defaultValue = "update,insert,delete,create,write,add,remove,merge,drop")
    private String                           methodAllowedWritePrefixes;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"select,read,query,find"</strong>).
     */
    @Parameter(required = true, defaultValue = "select,read,query,find")
    private String                           methodAllowedReadPrefixes;

    /**
     * The allow method name prefixes for writing methods (default:
     * <strong>"select,read,query,find"</strong>).
     */
    @Parameter(required = true, defaultValue = "call,execute")
    private String                           methodAllowedCallPrefixes;

    /**
     * Controls whether method names are validated according to
     * <strong>methodAllowedReadPrefixes</strong> and
     * <strong>methodAllowedWritePrefixes</strong> (default:
     * <strong>true</strong>).
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodValidateNamePrefixes;

    /**
     * The groupId to match for automatic RxJava detection (default:
     * <strong>"io.reactivex.rxjava2"</strong>).
     */
    @Parameter(required = true, defaultValue = "io.reactivex.rxjava2")
    private String                           rxJavaGroupId;

    /**
     * The artifactId to match for automatic RxJava detection (default:
     * <strong>"rxjava"</strong>).
     */
    @Parameter(required = true, defaultValue = "rxjava")
    private String                           rxJavaArtifactId;

    /**
     * The groupId to match for automatic Log4j detection (default:
     * <strong>"org.apache.logging.log4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.apache.logging.log4j")
    private String                           log4jGroupId;

    /**
     * The artifactId to match for automatic Log4j detection (default:
     * <strong>"log4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "log4j-api")
    private String                           log4jArtifactId;

    /**
     * The groupId to match for automatic SLF4j detection (default:
     * <strong>"org.slf4j"</strong>).
     */
    @Parameter(required = true, defaultValue = "org.slf4j")
    private String                           slf4jGroupId;

    /**
     * The artifactId to match for automatic SLF4j detection (default:
     * <strong>"slf4j-api"</strong>).
     */
    @Parameter(required = true, defaultValue = "slf4j-api")
    private String                           slf4jArtifactId;

    /**
     * The separator to split SQL statements inside a single .sql file (default:
     * <strong>";"</strong>).
     */
    @Parameter(required = true, defaultValue = ";")
    private String                           sqlStatementSeparator;

    /**
     * The charset to use while reading .sql files (default:
     * <strong>UTF-8</strong>).
     */
    @Parameter(required = true, defaultValue = "UTF-8")
    private String                           sqlFilesCharset;

    /**
     * The target Java source version (default: <strong>1.8</strong>). A value
     * lower than 1.8 disables the generation of the 'java.util.Stream' based
     * API.
     */
    @Parameter(required = true, defaultValue = "1.8")
    private String                           java;

    /**
     * The logging API to use (default: <strong>auto</strong> which picks the
     * logging API based on the projects dependencies). Possible other values
     * are "jdk", "log4j", "slf4j" and "none".
     */
    @Parameter(required = true, defaultValue = "auto")
    private String                           loggingApi;

    /**
     * Whether generated methods should catch SqlExceptions and rethrow them as
     * RuntimeExceptions (default: <strong>true</strong>). If set to
     * <strong>false</strong>, this will cause methods to declare that they
     * throw a checked exception which in turn will force all its users to
     * handle the exception.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          methodCatchAndRethrow;

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private final List<ParameterConverter>   parameterConverters  = new ArrayList<>();

    /**
     * Optional list of converters that are applied to input parameters.
     */
    @Parameter(required = false)
    private final List<ResultRowConverter>   resultRowConverters  = new ArrayList<>();

    /**
     * The default row converter which is being used if no custom converter is
     * specified for a statement. Can be either the alias or fully-qualified
     * name of a converter. Default 'resultRow'.
     */
    @Parameter(required = true, defaultValue = RESULT_ROW_CONVERTER)
    private String                           defaultRowConverter;

    /**
     * Controls whether {@link Generated} annotations should be added to the
     * generated repositories.
     */
    @Parameter(required = true, defaultValue = "true")
    private boolean                          classGeneratedAnnotation;

    /**
     * Controls whether {@link Generated} annotations should be added to the
     * generated fields.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean                          fieldGeneratedAnnotation;

    /**
     * Controls whether {@link Generated} annotations should be added to the
     * generated methods.
     */
    @Parameter(required = true, defaultValue = "false")
    private boolean                          methodGeneratedAnnotation;

    /**
     * Sets the comment used for {@link Generated} annotations.
     */
    @Parameter(required = true, defaultValue = "DO NOT EDIT")
    private String                           generatedAnnotationComment;

    @Parameter(property = "project", defaultValue = "${project}", readonly = true, required = true)
    private MavenProject                     project;

    private final PluginErrors               pluginErrors;
    private final PluginPreconditions        preconditions;
    private final FileSetResolver            fileSetResolver;
    private final SqlFileParser              sqlFileParser;
    private final RawJdbcRepositoryGenerator codeGenerator;
    private final PluginConfig               pluginConfig;
    private final DefaultUtilitiesGenerator  utilsGenerator;
    // private final PlexusContainer beanLocator;

    @Inject
    YoSqlGenerateMojo(
            final PluginErrors pluginErrors,
            final PluginPreconditions preconditions,
            final FileSetResolver fileSetResolver,
            final SqlFileParser sqlFileParser,
            final RawJdbcRepositoryGenerator codeGenerator,
            final PluginConfig pluginConfig,
            final DefaultUtilitiesGenerator utilsGenerator) {
        this.pluginErrors = pluginErrors;
        this.preconditions = preconditions;
        this.fileSetResolver = fileSetResolver;
        this.sqlFileParser = sqlFileParser;
        this.codeGenerator = codeGenerator;
        this.pluginConfig = pluginConfig;
        this.utilsGenerator = utilsGenerator;
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
        return timed("parse statements",
                () -> fileSetResolver.resolveFiles(Optional.ofNullable(sqlFiles).orElse(defaultSqlFileSet()))
                        .flatMap(sqlFileParser::parse)
                        .sorted(Comparator.comparing(SqlStatement::getName))
                        .collect(Collectors.toList()));
    }

    private FileSet defaultSqlFileSet() {
        final FileSet fileSet = new FileSet();
        fileSet.setDirectory(project.getBasedir().getAbsolutePath() + "/src/main/yosql");
        fileSet.addInclude("**/*.sql");
        return fileSet;
    }

    private void generateRepositories(final List<SqlStatement> allStatements) {
        timed("generate repositories", () -> allStatements.stream()
                .collect(groupingBy(SqlStatement::getRepository))
                .forEach(codeGenerator::generateRepository));
    }

    private void generateUtilities(final List<SqlStatement> allStatements) {
        timed("generate utilities", () -> utilsGenerator.generateUtilities(allStatements));
    }

    private void initializePluginConfig() {
        timed("initialize plugin config", () -> {
            final int parsedJavaVersion = Integer.parseInt(java.substring(java.length() - 1, java.length()));

            pluginConfig.setLogger(() -> getLog());

            pluginConfig.setOutputBaseDirectory(outputBaseDirectory);
            pluginConfig.setBasePackageName(basePackageName);
            pluginConfig.setUtilityPackageName(utilityPackageName);
            pluginConfig.setConverterPackageName(converterPackageName);
            pluginConfig.setSqlStatementSeparator(sqlStatementSeparator);
            pluginConfig.setSqlFilesCharset(sqlFilesCharset);

            pluginConfig.setRepositoryNameSuffix(repositoryNameSuffix);
            pluginConfig.setRepositoryGenerateInterface(repositoryGenerateInterface);
            pluginConfig.setRepositorySqlStatements(repositorySqlStatements);
            pluginConfig.setClassGeneratedAnnotation(classGeneratedAnnotation);
            pluginConfig.setFieldGeneratedAnnotation(fieldGeneratedAnnotation);
            pluginConfig.setMethodGeneratedAnnotation(methodGeneratedAnnotation);
            pluginConfig.setGeneratedAnnotationComment(generatedAnnotationComment);

            pluginConfig.setGenerateStandardApi(methodStandardApi);
            pluginConfig.setGenerateBatchApi(methodBatchApi);
            pluginConfig.setGenerateStreamEagerApi(parsedJavaVersion > 7 ? methodStreamEagerApi : false);
            pluginConfig.setGenerateStreamLazyApi(parsedJavaVersion > 7 ? methodStreamLazyApi : false);
            if (methodRxJavaApi == null) {
                pluginConfig.setGenerateRxJavaApi(project.getDependencies().stream().anyMatch(this::isRxJava2));
            } else {
                pluginConfig.setGenerateRxJavaApi(methodRxJavaApi);
            }
            if (loggingApi == null || "auto".equals(loggingApi.toLowerCase())) {
                if (project.getDependencies().stream().anyMatch(this::isLog4J)) {
                    pluginConfig.setLoggingApi(LoggingAPI.LOG4J);
                } else if (project.getDependencies().stream().anyMatch(this::isSlf4j)) {
                    pluginConfig.setLoggingApi(LoggingAPI.SLF4J);
                } else {
                    pluginConfig.setLoggingApi(LoggingAPI.JDK);
                }
            } else {
                try {
                    pluginConfig.setLoggingApi(LoggingAPI.valueOf(loggingApi.toUpperCase()));
                } catch (final IllegalArgumentException exception) {
                    pluginConfig.setLoggingApi(LoggingAPI.NONE);
                    pluginErrors.add(exception);
                }
            }

            pluginConfig.setMethodBatchPrefix(methodBatchPrefix);
            pluginConfig.setMethodBatchSuffix(methodBatchSuffix);
            pluginConfig.setMethodRxJavaPrefix(methodRxJavaPrefix);
            pluginConfig.setMethodRxJavaSuffix(methodRxJavaSuffix);
            pluginConfig.setMethodStreamPrefix(methodStreamPrefix);
            pluginConfig.setMethodStreamSuffix(methodStreamSuffix);
            pluginConfig.setMethodEagerName(methodEagerName);
            pluginConfig.setMethodLazyName(methodLazyName);
            pluginConfig.setMethodCatchAndRethrow(methodCatchAndRethrow);

            pluginConfig.setAllowedReadPrefixes(allowedReadPrefixes());
            pluginConfig.setAllowedWritePrefixes(allowedWritePrefixes());
            pluginConfig.setAllowedCallPrefixes(allowedCallPrefixes());
            pluginConfig.setValidateMethodNamePrefixes(methodValidateNamePrefixes);

            final String utilPackage = pluginConfig.getBasePackageName() + "." + pluginConfig.getUtilityPackageName();
            pluginConfig.setFlowStateClass(ClassName.get(utilPackage, FlowStateGenerator.FLOW_STATE_CLASS_NAME));
            pluginConfig.setResultStateClass(ClassName.get(utilPackage, ResultStateGenerator.RESULT_STATE_CLASS_NAME));
            pluginConfig.setResultRowClass(ClassName.get(utilPackage, ResultRowGenerator.RESULT_ROW_CLASS_NAME));

            final ResultRowConverter toResultRow = new ResultRowConverter();
            toResultRow.setAlias(RESULT_ROW_CONVERTER);
            toResultRow.setResultType(utilPackage + "." + ResultRowGenerator.RESULT_ROW_CLASS_NAME);
            toResultRow.setConverterType(
                    pluginConfig.getBasePackageName()
                            + "."
                            + pluginConfig.getConverterPackageName()
                            + "."
                            + ToResultRowConverterGenerator.TO_RESULT_ROW_CONVERTER_CLASS_NAME);
            resultRowConverters.add(toResultRow);
            pluginConfig.setResultRowConverters(resultRowConverters);
            pluginConfig.setDefaultRowConverter(defaultRowConverter);
        });
    }

    private void timed(final String taskName, final Runnable task) {
        final Instant preRun = Instant.now();
        task.run();
        final Instant postRun = Instant.now();
        if (getLog().isDebugEnabled()) {
            final String message = String.format("Time spent running [%s]: %s (ms)",
                    taskName, Duration.between(preRun, postRun).toMillis());
            getLog().debug(message);
        }
    }

    private <T> T timed(final String taskName, final Supplier<T> supplier) {
        final Instant preRun = Instant.now();
        final T value = supplier.get();
        final Instant postRun = Instant.now();
        if (getLog().isDebugEnabled()) {
            final String message = String.format("Time spent running [%s]: %s (ms)",
                    taskName, Duration.between(preRun, postRun).toMillis());
            getLog().debug(message);
        }
        return value;
    }

    private String[] allowedCallPrefixes() {
        return splitIntoArray(methodAllowedCallPrefixes);
    }

    private String[] allowedWritePrefixes() {
        return splitIntoArray(methodAllowedWritePrefixes);
    }

    private String[] allowedReadPrefixes() {
        return splitIntoArray(methodAllowedReadPrefixes);
    }

    private String[] splitIntoArray(final String input) {
        return Arrays.stream(input.split(","))
                .filter(Objects::nonNull)
                .map(String::trim)
                .toArray(String[]::new);
    }

    private boolean isRxJava2(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, rxJavaGroupId, rxJavaArtifactId);
    }

    private boolean isLog4J(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, log4jGroupId, log4jArtifactId);
    }

    private boolean isSlf4j(final Dependency dependency) {
        return matchesGroupAndArtifact(dependency, slf4jGroupId, slf4jArtifactId);
    }

    private static boolean matchesGroupAndArtifact(
            final Dependency dependency,
            final String groupId,
            final String artifactId) {
        return groupId.equals(dependency.getGroupId())
                && artifactId.equals(dependency.getArtifactId());
    }

}
