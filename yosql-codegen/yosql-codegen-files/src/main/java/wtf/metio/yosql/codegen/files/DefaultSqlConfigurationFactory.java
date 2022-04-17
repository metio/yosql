/*
 * This file is part of yosql. It is subject to the license terms in the LICENSE file found in the top-level
 * directory of this distribution and at https://creativecommons.org/publicdomain/zero/1.0/. No part of yosql,
 * including this file, may be copied, modified, propagated, or distributed except according to the terms contained
 * in the LICENSE file.
 */
package wtf.metio.yosql.codegen.files;

import ch.qos.cal10n.IMessageConveyor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.slf4j.cal10n.LocLogger;
import wtf.metio.yosql.codegen.errors.ExecutionErrors;
import wtf.metio.yosql.codegen.errors.ValidationErrors;
import wtf.metio.yosql.codegen.lifecycle.RepositoryLifecycle;
import wtf.metio.yosql.internals.jdk.Strings;
import wtf.metio.yosql.models.constants.sql.ReturningMode;
import wtf.metio.yosql.models.constants.sql.SqlType;
import wtf.metio.yosql.models.immutables.RuntimeConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;
import wtf.metio.yosql.models.sql.ResultRowConverter;
import wtf.metio.yosql.models.sql.SqlParameter;

import javax.lang.model.SourceVersion;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static wtf.metio.yosql.models.constants.sql.SqlType.*;

public final class DefaultSqlConfigurationFactory implements SqlConfigurationFactory {

    private final LocLogger logger;
    private final RuntimeConfiguration runtimeConfiguration;
    private final ExecutionErrors errors;
    private final IMessageConveyor messages;

    /**
     * @param logger               The logger to use.
     * @param runtimeConfiguration The runtime config to use.
     * @param errors               The error collector to use.
     * @param messages             The messages to use.
     */
    public DefaultSqlConfigurationFactory(
            final LocLogger logger,
            final RuntimeConfiguration runtimeConfiguration,
            final ExecutionErrors errors, IMessageConveyor messages) {
        this.runtimeConfiguration = runtimeConfiguration;
        this.errors = errors;
        this.logger = logger;
        this.messages = messages;
    }

    /**
     * @param source           The source file of the statement.
     * @param yaml             The YAML front matter of the statement.
     * @param parameterIndices The parameter indices (if any) of the statement.
     * @param statementInFile  The counter for statements with the same name in the same source file.
     * @return The resulting configuration.
     */
    @Override
    public SqlConfiguration createConfiguration(
            final Path source,
            final String yaml,
            final Map<String, List<Integer>> parameterIndices,
            final int statementInFile) {
        final var userConfiguration = loadConfig(yaml);
        logConfiguration(userConfiguration);
        final var baseConfiguration = apply(userConfiguration, List.of(
                configuration -> name(configuration, source, statementInFile),
                this::generateBatchApi,
                this::batchNamePrefix,
                this::batchNameSuffix,
                this::streamNamePrefix,
                this::streamNameSuffix,
                this::rxJavaNamePrefix,
                this::rxJavaNameSuffix,
                this::lazyName,
                this::eagerName,
                this::type,
                this::returningMode
        ));
        validateNames(source, baseConfiguration);
        return apply(baseConfiguration, List.of(
                configuration -> name(configuration, source, statementInFile),
                this::standard,
                this::batch,
                this::streamEager,
                this::streamLazy,
                this::rxJava,
                this::catchAndRethrow,
                configuration -> repository(source, configuration),
                configuration -> parameters(source, parameterIndices, configuration),
                this::resultConverter  // TODO: Configure parameter converters
        ));
    }

    private SqlConfiguration apply(
            final SqlConfiguration configuration,
            final List<Function<SqlConfiguration, SqlConfiguration>> transformers) {
        var transformed = configuration;
        for (final var transformer : transformers) {
            transformed = transformer.apply(transformed);
        }
        logConfiguration(configuration);
        return transformed;
    }

    private void logConfiguration(final SqlConfiguration configuration) {
        logger.debug("SQL CONFIGURATION:");
        logger.debug("name:             {}", configuration.name());
        logger.debug("repository:       {}", configuration.repository());
        logger.debug("parameters:       {}", configuration.parameters().size());
        logger.debug("standardApi:      {}", configuration.generateStandardApi());
        logger.debug("batchApi:         {}", configuration.generateBatchApi());
        logger.debug("rxJava2Api:       {}", configuration.generateRxJavaApi());
        logger.debug("eagerStreamApi:   {}", configuration.generateStreamEagerApi());
        logger.debug("lazyStreamApi:    {}", configuration.generateStreamLazyApi());
        logger.debug("rxJava2Api:       {}", configuration.generateRxJavaApi());
        logger.debug("type:             {}", configuration.type());
        logger.debug("returningMode:    {}", configuration.returningMode());
        logger.debug("vendor:           {}", configuration.vendor());
    }

    private SqlConfiguration name(final SqlConfiguration configuration, final Path source, final int statementInFile) {
        if (nullOrEmpty(configuration.name())) {
            final var fileName = getFileNameWithoutExtension(source);
            final var validName = SourceVersion.isName(fileName) ? fileName : generateName(configuration);
            return SqlConfiguration.copyOf(configuration)
                    .withName(calculateName(validName, statementInFile));
        }
        if (SourceVersion.isName(configuration.name())) {
            return configuration;
        }
        return SqlConfiguration.copyOf(configuration)
                .withName(calculateName(generateName(configuration), statementInFile));
    }

    private static String calculateName(final String name, final int statementInFile) {
        return statementInFile > 1 ? name + statementInFile : name;
    }

    private String generateName(final SqlConfiguration configuration) {
        final var typeLookup = switch (configuration.type()) {
            case READING -> runtimeConfiguration.repositories().allowedReadPrefixes().get(0);
            case WRITING -> runtimeConfiguration.repositories().allowedWritePrefixes().get(0);
            case CALLING -> runtimeConfiguration.repositories().allowedCallPrefixes().get(0);
            case UNKNOWN -> "statement";
        };
        return typeLookup + "NameWasChanged";
    }

    private SqlConfiguration generateBatchApi(final SqlConfiguration configuration) {
        if (configuration.generateBatchApi()) {
            return configuration;
        }
        return SqlConfiguration.copyOf(configuration)
                .withGenerateBatchApi(runtimeConfiguration.repositories().generateBatchApi());
    }

    private SqlConfiguration batchNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.batchPrefix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withBatchPrefix(runtimeConfiguration.repositories().batchPrefix());
        }
        return configuration;
    }

    private SqlConfiguration batchNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.batchSuffix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withBatchSuffix(runtimeConfiguration.repositories().batchSuffix());
        }
        return configuration;
    }

    private SqlConfiguration streamNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.streamPrefix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withStreamPrefix(runtimeConfiguration.repositories().streamPrefix());
        }
        return configuration;
    }

    private SqlConfiguration streamNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.streamSuffix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withStreamSuffix(runtimeConfiguration.repositories().streamSuffix());
        }
        return configuration;
    }

    private SqlConfiguration rxJavaNamePrefix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.rxjava2Prefix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withRxjava2Prefix(runtimeConfiguration.repositories().rxjava2Prefix());
        }
        return configuration;
    }

    private SqlConfiguration rxJavaNameSuffix(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.rxjava2Suffix())) {
            return SqlConfiguration.copyOf(configuration)
                    .withRxjava2Suffix(runtimeConfiguration.repositories().rxjava2Suffix());
        }
        return configuration;
    }

    private SqlConfiguration lazyName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.lazyName())) {
            return SqlConfiguration.copyOf(configuration)
                    .withLazyName(runtimeConfiguration.repositories().lazyName());
        }
        return configuration;
    }

    private SqlConfiguration eagerName(final SqlConfiguration configuration) {
        if (nullOrEmpty(configuration.eagerName())) {
            return SqlConfiguration.copyOf(configuration)
                    .withEagerName(runtimeConfiguration.repositories().eagerName());
        }
        return configuration;
    }

    private SqlConfiguration type(final SqlConfiguration configuration) {
        if (configuration.type() == null || UNKNOWN.equals(configuration.type())) {
            return SqlConfiguration.copyOf(configuration)
                    .withType(mapNameToType(configuration.name()));
        }
        return configuration;
    }

    private SqlType mapNameToType(final String name) {
        final var repositories = runtimeConfiguration.repositories();
        if (startsWith(name, repositories.allowedWritePrefixes())) {
            return WRITING;
        } else if (startsWith(name, repositories.allowedReadPrefixes())) {
            return READING;
        } else if (startsWith(name, repositories.allowedCallPrefixes())) {
            return CALLING;
        }
        return UNKNOWN;
    }

    private SqlConfiguration returningMode(final SqlConfiguration configuration) {
        if (configuration.returningMode() == null || configuration.returningMode() == ReturningMode.NONE) {
            return SqlConfiguration.copyOf(configuration)
                    .withReturningMode(mapTypeReturningMode(configuration.type()));
        }
        return configuration;
    }

    private ReturningMode mapTypeReturningMode(final SqlType type) {
        return switch (type) {
            case READING -> ReturningMode.LIST;
            case CALLING -> ReturningMode.FIRST;
            default -> ReturningMode.NONE;
        };
    }

    private void validateNames(final Path source, final SqlConfiguration configuration) {
        if (runtimeConfiguration.repositories().validateMethodNamePrefixes()) {
            switch (configuration.type()) {
                case READING:
                    if (!startsWith(configuration.name(), runtimeConfiguration.repositories().allowedReadPrefixes())) {
                        invalidPrefix(source, READING, configuration.name());
                    }
                    break;
                case WRITING:
                    if (!startsWith(configuration.name(), runtimeConfiguration.repositories().allowedWritePrefixes())) {
                        invalidPrefix(source, WRITING, configuration.name());
                    }
                    break;
                case CALLING:
                    if (!startsWith(configuration.name(), runtimeConfiguration.repositories().allowedCallPrefixes())) {
                        invalidPrefix(source, CALLING, configuration.name());
                    }
                    break;
                default:
                    errors.illegalArgument(messages.getMessage(ValidationErrors.UNSUPPORTED_TYPE, source, configuration.type()));
                    break;
            }
        }
    }

    private void invalidPrefix(final Path source, final SqlType sqlType, final String name) {
        errors.illegalArgument(messages.getMessage(ValidationErrors.INVALID_PREFIX, source, sqlType, name));
    }

    private SqlConfiguration standard(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withGenerateStandardApi(runtimeConfiguration.repositories().generateStandardApi());
    }

    private SqlConfiguration batch(final SqlConfiguration configuration) {
        if (READING == configuration.type()) {
            // TODO: allow batched select statements
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateBatchApi(false);
        }
        return SqlConfiguration.copyOf(configuration)
                .withGenerateBatchApi(runtimeConfiguration.repositories().generateBatchApi());
    }

    private SqlConfiguration streamEager(final SqlConfiguration configuration) {
        if (WRITING == configuration.type()) {
            // TODO: allow eagerly streamed insert/update statements
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateStreamEagerApi(false);
        }
        return SqlConfiguration.copyOf(configuration)
                .withGenerateStreamEagerApi(runtimeConfiguration.repositories().generateStreamEagerApi());
    }

    private SqlConfiguration streamLazy(final SqlConfiguration configuration) {
        if (WRITING == configuration.type()) {
            // TODO: allow lazily streamed insert/update statements
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateStreamLazyApi(false);
        }
        return SqlConfiguration.copyOf(configuration)
                .withGenerateStreamLazyApi(runtimeConfiguration.repositories().generateStreamLazyApi());
    }

    private SqlConfiguration rxJava(final SqlConfiguration configuration) {
        if (WRITING == configuration.type()) {
            // TODO: allow rxjava2 insert/update statements
            return SqlConfiguration.copyOf(configuration)
                    .withGenerateRxJavaApi(false);
        }
        return SqlConfiguration.copyOf(configuration)
                .withGenerateRxJavaApi(runtimeConfiguration.repositories().generateRxJavaApi());
    }

    private SqlConfiguration catchAndRethrow(final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withCatchAndRethrow(runtimeConfiguration.repositories().catchAndRethrow());
    }

    private SqlConfiguration repository(final Path source, final SqlConfiguration configuration) {
        return SqlConfiguration.copyOf(configuration)
                .withRepository(repositoryName(source, configuration));
    }

    private String repositoryName(final Path source, final SqlConfiguration configuration) {
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_INPUT, runtimeConfiguration.files().inputBaseDirectory());
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_SOURCE, source);
        final var relativePathToSqlFile = runtimeConfiguration.files().inputBaseDirectory().relativize(source);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RELATIVE, relativePathToSqlFile);
        final var rawRepositoryName = relativePathToSqlFile.getParent().toString();
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_RAW, rawRepositoryName);
        final var dottedRepositoryName = rawRepositoryName.replace(File.separator, ".");
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_DOTTED, dottedRepositoryName);
        final var upperCaseName = upperCaseFirstLetterInLastSegment(dottedRepositoryName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_UPPER, upperCaseName);
        final var actualRepository = repositoryInBasePackage(upperCaseName);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_ACTUAL, actualRepository);
        final var repository = repositoryWithNameSuffix(actualRepository);
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_NAME, repository);
        final var userGivenRepository = repositoryWithNameSuffix(
                repositoryInBasePackage(configuration.repository()));
        logger.debug(RepositoryLifecycle.REPOSITORY_NAME_CALC_NAME, userGivenRepository);
        return SqlConfiguration.usingDefaults().build().repository().equals(configuration.repository())
                ? repository : userGivenRepository;
    }

    private static String upperCaseFirstLetterInLastSegment(final String name) {
        if (name.contains(".")) {
            return name.substring(0, name.lastIndexOf('.') + 1)
                    + name.substring(name.lastIndexOf('.') + 1, name.lastIndexOf('.') + 2).toUpperCase()
                    + name.substring(name.lastIndexOf('.') + 2);
        }
        return Strings.upperCase(name);
    }

    private String repositoryWithNameSuffix(final String repository) {
        return repository.endsWith(runtimeConfiguration.repositories().repositoryNameSuffix())
                ? repository
                : repository + runtimeConfiguration.repositories().repositoryNameSuffix();
    }

    private String repositoryInBasePackage(final String repository) {
        return repository.startsWith(runtimeConfiguration.repositories().basePackageName())
                ? repository
                : runtimeConfiguration.repositories().basePackageName() + "." + repository;
    }

    private static boolean isMissingParameter(final List<SqlParameter> parameters, final String parameterName) {
        return Stream.ofNullable(parameters).flatMap(Collection::stream).noneMatch(nameMatches(parameterName));
    }

    private static List<SqlParameter> updateIndices(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        return parameters.stream()
                .map(parameter -> SqlParameter.builder()
                        .setName(parameter.name())
                        .setType(parameter.type())
                        .setIndices(asIntArray(indices.get(parameter.name())))
                        .setConverter(parameter.converter())
                        .build())
                .collect(Collectors.toList());
    }

    private static int[] asIntArray(final List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private static boolean methodNamesMatch(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.methodName().equals(resultConverter.methodName());
    }

    private static Predicate<? super SqlParameter> nameMatches(final String parameterName) {
        return parameter -> parameterName.equals(parameter.name());
    }

    private static SqlConfiguration loadConfig(final String yaml) {
        SqlConfiguration configuration = null;
        try {
            if (!yaml.isBlank()) {
                final var yoSqlModule = new SimpleModule();
                yoSqlModule.addDeserializer(SqlParameter.class, new SqlParameterDeserializer());
                yoSqlModule.addDeserializer(ResultRowConverter.class, new ResultRowConverterDeserializer());
                final var yamlMapper = YAMLMapper.builder()
                        .addModule(new Jdk8Module())
                        .addModule(yoSqlModule)
                        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                        .build();
                configuration = yamlMapper
                        .readValue(yaml, SqlConfiguration.class);
            }
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
        if (configuration == null) {
            configuration = SqlConfiguration.usingDefaults().build();
        }
        return configuration;
    }

    private SqlConfiguration parameters(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final var currentParameters = configuration.parameters();
        if (parametersAreValid(source, parameterIndices, configuration)) {
            final var updatedParameters = updateIndices(currentParameters, parameterIndices);
            final var allParameters = addMissingParameters(updatedParameters, parameterIndices);
            return SqlConfiguration.copyOf(configuration)
                    .withParameters(allParameters);
        }
        return configuration;
    }

    private List<SqlParameter> addMissingParameters(final List<SqlParameter> parameters, final Map<String, List<Integer>> indices) {
        final var all = new ArrayList<>(parameters);
        for (final var entry : indices.entrySet()) {
            final var parameterName = entry.getKey();
            if (isMissingParameter(all, parameterName)) {
                all.add(SqlParameter.builder()
                        .setName(parameterName)
                        .setType("java.lang.Object")
                        .setIndices(asIntArray(entry.getValue()))
                        .build());
            }
        }
        return all;
    }

    private String getDefaultAlias(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> converterTypeMatches(resultConverter, converter),
                ResultRowConverter::alias);
    }

    private String getDefaultConverterType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter),
                ResultRowConverter::converterType);
    }

    private String getDefaultResultType(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::resultType);
    }

    private String getDefaultMethodName(final ResultRowConverter resultConverter) {
        return getConverterFieldOrEmptyString(
                converter -> aliasMatches(resultConverter, converter)
                        || converterTypeMatches(resultConverter, converter),
                ResultRowConverter::methodName);
    }

    private static boolean aliasMatches(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.alias().equals(resultConverter.alias());
    }

    private SqlConfiguration resultConverter(final SqlConfiguration configuration) {
        if (configuration.resultRowConverter().isEmpty()) {
            return SqlConfiguration.copyOf(configuration).withResultRowConverter(getDefaultRowConverter());
        }
        final var currentConverter = configuration.resultRowConverter().get();
        final var converter = ResultRowConverter.builder()
                .setAlias(Strings.isBlank(currentConverter.alias()) ?
                        getDefaultAlias(currentConverter) : currentConverter.alias())
                .setConverterType(Strings.isBlank(currentConverter.converterType()) ?
                        getDefaultConverterType(currentConverter) : currentConverter.converterType())
                .setMethodName(Strings.isBlank(currentConverter.methodName()) ?
                        getDefaultMethodName(currentConverter) : currentConverter.methodName())
                .setResultType(Strings.isBlank(currentConverter.resultType()) ?
                        getDefaultResultType(currentConverter) : currentConverter.resultType())
                .build();

        return SqlConfiguration.copyOf(configuration)
                .withResultRowConverter(converter);
    }

    private static boolean converterTypeMatches(
            final ResultRowConverter resultConverter,
            final ResultRowConverter converter) {
        return converter.converterType().equals(resultConverter.converterType());
    }

    private Optional<ResultRowConverter> getDefaultRowConverter() {
        final var resultRowConverter = runtimeConfiguration.jdbc().defaultConverter();
        return runtimeConfiguration.jdbc().userTypes().stream()
                .filter(converter -> resultRowConverter.isEmpty() || resultRowConverter.get().equals(converter))
                .findFirst()
                .or(() -> resultRowConverter);
    }

    private String getConverterFieldOrEmptyString(
            final Predicate<ResultRowConverter> predicate,
            final Function<ResultRowConverter, String> mapper) {
        return runtimeConfiguration.jdbc().userTypes().stream()
                .filter(predicate)
                .map(mapper)
                .findFirst()
                .orElse("");
    }

    private static boolean nullOrEmpty(final String object) {
        return object == null || object.isEmpty();
    }

    private static boolean startsWith(final String fileName, final List<String> prefixes) {
        return prefixes != null && prefixes.stream().anyMatch(fileName::startsWith);
    }

    private boolean parametersAreValid(
            final Path source,
            final Map<String, List<Integer>> parameterIndices,
            final SqlConfiguration configuration) {
        final var parameterErrors = Stream.ofNullable(configuration.parameters())
                .flatMap(Collection::stream)
                .filter(param -> !parameterIndices.containsKey(param.name()))
                .map(param -> messages.getMessage(ValidationErrors.UNKNOWN_PARAMETER, source, param.name()))
                .peek(errors::illegalArgument)
                .peek(logger::error)
                .collect(Collectors.toList());
        return parameterErrors.isEmpty();
    }

    private static String getFileNameWithoutExtension(final Path path) {
        final var fileName = path.getFileName().toString();
        final var dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

}
