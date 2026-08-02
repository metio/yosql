/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.files;

import com.palantir.javapoet.ClassName;
import wtf.metio.yosql.codegen.exceptions.MissingConverterSourceException;
import wtf.metio.yosql.codegen.exceptions.UnusableConverterException;
import wtf.metio.yosql.codegen.records.RecordConverterNames;
import wtf.metio.yosql.codegen.records.RecordScanner;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.SqlConfiguration;

import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Resolves the converter a statement is generated against.
 *
 * <p>A statement either names a converter class, names a record to have one written for it, or
 * names neither and takes the build's default. Whichever it is, the generator needs all four parts
 * of a {@link ResultRowConverter}, and only one of them — the class — is ever configured. The rest
 * is read back out of that class, so what the repository calls is what the converter actually
 * declares.</p>
 */
public final class DefaultMethodResultRowConverterConfigurer implements MethodResultRowConverterConfigurer {

    private final ConverterConfiguration converters;
    private final RecordConverterNames recordConverters;
    private final RecordScanner scanner;

    public DefaultMethodResultRowConverterConfigurer(
            final ConverterConfiguration converters,
            final RecordConverterNames recordConverters,
            final RecordScanner scanner) {
        this.converters = converters;
        this.recordConverters = recordConverters;
        this.scanner = scanner;
    }

    @Override
    public SqlConfiguration configureResultRowConverter(final SqlConfiguration configuration) {
        final var origin = "Statement '%s'".formatted(configuration.name().orElse("<unnamed>"));
        return SqlConfiguration.copyOf(configuration)
                .withResultRowConverter(configuration.resultRowConverter()
                        .map(converter -> resolve(converter, origin))
                        .or(() -> generatedRecordConverter(configuration))
                        .or(this::defaultConverter));
    }

    /**
     * Points the statement at the converter that will be generated for its {@code resultRowType}.
     * The repository then treats it like any other converter — a field of that type, called by that
     * name — which is why naming a record needs no other configuration.
     *
     * <p>A statement that names both a converter and a record type keeps the converter: naming a
     * converter names the exact code to call, and there is nothing to infer.</p>
     */
    private Optional<ResultRowConverter> generatedRecordConverter(final SqlConfiguration configuration) {
        return configuration.resultRowType()
                .map(String::strip)
                .filter(Predicate.not(String::isEmpty))
                .map(ClassName::bestGuess)
                .map(type -> ResultRowConverter.builder()
                        .setAlias(recordConverters.alias(type))
                        .setConverterType(recordConverters.converterClass(type).toString())
                        .setMethodName(recordConverters.methodName())
                        .setResultType(type.toString())
                        .build());
    }

    private Optional<ResultRowConverter> defaultConverter() {
        return converters.defaultConverter()
                .map(converter -> resolve(converter, "The 'defaultConverter' configuration option"));
    }

    /**
     * Fills in everything the generator needs from the converter's own source.
     *
     * <p>A converter that already carries all four parts is passed through untouched: that is the
     * generated ToMap converter, which has no source to read because it does not exist yet.</p>
     */
    private ResultRowConverter resolve(final ResultRowConverter converter, final String origin) {
        if (isFullyConfigured(converter)) {
            return converter;
        }
        final var type = converterType(converter, origin);
        final var location = scanner.locationOf(type);
        final var source = scanner.scan(type)
                .orElseThrow(() -> new MissingConverterSourceException(type, location, origin));
        final var methods = source.resultSetMethods();
        if (methods.isEmpty()) {
            throw new UnusableConverterException(origin, location,
                    "'%s' declares no public method taking a 'java.sql.ResultSet'".formatted(type));
        }
        if (methods.size() > 1) {
            throw new UnusableConverterException(origin, location,
                    "'%s' declares %d of them: %s".formatted(type, methods.size(), methods.stream()
                            .map(method -> method.name() + "(ResultSet)")
                            .collect(Collectors.joining(", "))));
        }
        final var method = methods.getFirst();
        return ResultRowConverter.builder()
                .setAlias(alias(type))
                .setConverterType(type.toString())
                .setMethodName(method.name())
                .setResultType(method.returnType().toString())
                .build();
    }

    private static ClassName converterType(final ResultRowConverter converter, final String origin) {
        final var name = converter.converterType()
                .map(String::strip)
                .filter(Predicate.not(String::isEmpty))
                .orElseThrow(() -> new UnusableConverterException(origin, "no class name was given"));
        try {
            return ClassName.bestGuess(name);
        } catch (final IllegalArgumentException _) {
            throw new UnusableConverterException(origin, "'%s' is not a class name".formatted(name));
        }
    }

    /**
     * The field a repository injects the converter into, named after the converter's own class so
     * that reading the repository tells you which converter runs.
     */
    private static String alias(final ClassName type) {
        final var simpleName = type.simpleName();
        return simpleName.substring(0, 1).toLowerCase(Locale.ROOT) + simpleName.substring(1);
    }

    private static boolean isFullyConfigured(final ResultRowConverter converter) {
        return converter.alias().isPresent()
                && converter.converterType().isPresent()
                && converter.methodName().isPresent()
                && converter.resultType().isPresent();
    }

}
