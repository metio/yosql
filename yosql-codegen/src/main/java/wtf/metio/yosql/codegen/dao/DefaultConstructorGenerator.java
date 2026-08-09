/*
 * SPDX-FileCopyrightText: The yosql Authors
 * SPDX-License-Identifier: 0BSD
 */

package wtf.metio.yosql.codegen.dao;

import wtf.metio.yosql.models.configuration.GeneratedNames;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.MethodSpec;
import wtf.metio.yosql.codegen.blocks.CodeBlocks;
import wtf.metio.yosql.codegen.blocks.Methods;
import wtf.metio.yosql.codegen.exceptions.MissingConverterAliasException;
import wtf.metio.yosql.codegen.exceptions.MissingDefaultConverterException;
import wtf.metio.yosql.models.configuration.ResultRowConverter;
import wtf.metio.yosql.models.immutables.ConverterConfiguration;
import wtf.metio.yosql.models.immutables.RepositoriesConfiguration;
import wtf.metio.yosql.models.immutables.SqlStatement;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of the {@link ConstructorGenerator} interface.
 */
public final class DefaultConstructorGenerator implements ConstructorGenerator {

    private final CodeBlocks blocks;
    private final Methods methods;
    private final JdbcParameters jdbcParameters;
    private final RepositoriesConfiguration repositories;
    private final ConverterConfiguration converters;

    public DefaultConstructorGenerator(
            final CodeBlocks blocks,
            final Methods methods,
            final JdbcParameters jdbcParameters,
            final RepositoriesConfiguration repositories,
            final ConverterConfiguration converters) {
        this.blocks = blocks;
        this.methods = methods;
        this.jdbcParameters = jdbcParameters;
        this.repositories = repositories;
        this.converters = converters;
    }

    @Override
    public MethodSpec repository(final List<SqlStatement> statements) {
        final var builder = CodeBlock.builder();
        final var constructor = methods.constructor();

        if (RepositoryConnections.needsDataSource(repositories, statements)) {
            constructor.addParameter(jdbcParameters.dataSource())
                    .addCode(blocks.initializeFieldToSelf(GeneratedNames.DATA_SOURCE));
        }

        final var injected = injectedConverters(statements);
        resultConverters(statements).forEach(converter -> {
            if (repositories.injectConverters() || injected.contains(converter)) {
                constructor.addParameter(jdbcParameters.converter(converter));
                builder.add(blocks.initializeFieldToSelf(converter.alias()
                        .orElseThrow(MissingConverterAliasException::new)));
            } else {
                builder.add(blocks.initializeConverter(converter));
            }
        });

        return constructor
                .addCode(builder.build())
                .build();
    }

    /**
     * The converters a statement of this repository asked to be given rather than to construct.
     *
     * <p>Asked per statement and answered per converter, because a converter is a field of the
     * repository and a field is initialised once. Statements sharing one therefore share the answer,
     * and one of them asking settles it: a converter that has to be handed in cannot also be built
     * here, while one that is handed in satisfies every statement that would have built it.</p>
     */
    private Set<ResultRowConverter> injectedConverters(final List<SqlStatement> statements) {
        final var asked = statements.stream()
                .map(SqlStatement::getConfiguration)
                .filter(configuration -> configuration.injectConverter().orElse(Boolean.FALSE))
                .toList();
        return SqlStatement.resultConverters(
                        statements.stream()
                                .filter(statement -> asked.contains(statement.getConfiguration()))
                                .toList(),
                        converters.defaultConverter().orElseThrow(MissingDefaultConverterException::new))
                .collect(Collectors.toSet());
    }

    private Stream<ResultRowConverter> resultConverters(final List<SqlStatement> statements) {
        return SqlStatement.resultConverters(statements, converters.defaultConverter()
                .orElseThrow(MissingDefaultConverterException::new));
    }

}
